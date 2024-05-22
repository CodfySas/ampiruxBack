package com.osia.nota_maestro.service.calendarTask.impl

import com.osia.nota_maestro.dto.calendar.v1.CalendarDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskMapper
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskRequest
import com.osia.nota_maestro.repository.calendar.CalendarRepository
import com.osia.nota_maestro.repository.resource.ResourceRepository
import com.osia.nota_maestro.service.calendarTask.CalendarTaskService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.UUID

@Service("calendar.crud_service")
@Transactional
class CalendarTaskServiceImpl(
    private val calendarMapper: CalendarTaskMapper,
    private val resourceRepository: ResourceRepository,
    private val calendarRepository: CalendarRepository,
) : CalendarTaskService {

    override fun getCalendarMonth(year: Int, month: Int, school: UUID): List<List<CalendarDto>> {
        val maxDays = Month.of(month).length(Year.isLeap(year.toLong()))
        val dateInit = LocalDate.of(year, month, 1)
        val dateFinish = LocalDate.of(year, month, maxDays)

        val tasks = calendarRepository.findAllByDayBetweenAndUuidSchoolOrderByDayAsc(dateInit, dateFinish, school)
        val resourceTasks = tasks.filter { it.uuidResource != null }
        val resources = resourceRepository.getByUuidIn(resourceTasks.mapNotNull { it.uuidResource })
        val finalList = mutableListOf<CalendarDto>()

        if (dateInit.dayOfWeek != DayOfWeek.MONDAY) {
            val initDayWeek = dateInit.dayOfWeek.value - 1
            repeat(initDayWeek) {
                val dayExtraMinus = dateInit.minusDays((initDayWeek - it).toLong())
                finalList.add(
                    CalendarDto().apply {
                        this.outOfMonth = true
                        this.dayOfWeek = dayExtraMinus.dayOfWeek
                        this.dayNumber = dayExtraMinus.dayOfMonth
                        this.totalDay = dayExtraMinus
                        this.tasks = mutableListOf()
                    }
                )
            }
        }

        for (day in 1..maxDays) {
            val dayTasks = resourceTasks.filter { it.day?.dayOfMonth == day }.map(calendarMapper::toDto).sortedBy { it.hourInit }
            dayTasks.forEach {
                it.name = resources.firstOrNull {
                    resource ->
                    resource.uuid == it.uuidResource
                }?.name
            }
            val actualDate = dateInit.plusDays((day - 1).toLong())

            finalList.add(
                CalendarDto().apply {
                    this.dayNumber = day
                    this.dayOfWeek = DayOfWeek.from(actualDate)
                    this.outOfMonth = false
                    this.totalDay = actualDate
                    this.tasks = dayTasks
                }
            )
        }

        if (dateFinish.dayOfWeek != DayOfWeek.SUNDAY) {
            val daysToAdd = 7 - (dateFinish.dayOfWeek.value)
            repeat(daysToAdd) {
                val dayExtraPlus = dateFinish.plusDays(it.toLong() + 1)
                finalList.add(
                    CalendarDto().apply {
                        this.outOfMonth = true
                        this.dayOfWeek = dayExtraPlus.dayOfWeek
                        this.dayNumber = dayExtraPlus.dayOfMonth
                        this.totalDay = dayExtraPlus
                        this.tasks = mutableListOf()
                    }
                )
            }
        }
        val groupedByFileList = mutableListOf<List<CalendarDto>>()
        val fileData = mutableListOf<CalendarDto>()
        finalList.forEach {
            fileData.add(it)
            if (it.dayOfWeek == DayOfWeek.SUNDAY) {
                groupedByFileList.add(fileData.toMutableList())
                fileData.clear()
            }
        }
        return groupedByFileList
    }

    override fun getCalendarDay(year: Int, month: Int, day: Int, school: UUID): CalendarDto {
        val dateInit = LocalDate.of(year, month, day)
        val dateFinish = LocalDate.of(year, month, day)

        val tasks = calendarRepository.findAllByDayBetweenAndUuidSchoolOrderByDayAsc(dateInit, dateFinish, school)
        val resourceTask = tasks.filter { it.uuidResource != null }
        return CalendarDto().apply {
            this.dayNumber = day
            this.dayOfWeek = DayOfWeek.from(dateInit)
            this.outOfMonth = false
            this.totalDay = dateInit
            this.tasks = resourceTask.map(calendarMapper::toDto)
        }
    }

    override fun submitTask(school: UUID, calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto {
        calendarTaskRequest.uuidSchool = school
        return calendarMapper.toDto(calendarRepository.save(calendarMapper.toModel(calendarTaskRequest)))
    }

    override fun updateTask(school: UUID, uuid: UUID, calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto {
        calendarTaskRequest.uuidSchool = school
        val calendarToUpdate = calendarRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        calendarMapper.update(calendarTaskRequest, calendarToUpdate)
        return calendarMapper.toDto(calendarRepository.save(calendarToUpdate))
    }
}
