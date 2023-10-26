package com.osia.nota_maestro.service.calendarTask.impl

import com.osia.nota_maestro.dto.calendar.v1.CalendarDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskMapper
import com.osia.nota_maestro.repository.calendar.CalendarRepository
import com.osia.nota_maestro.service.calendarTask.CalendarTaskService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

@Service("calendar.crud_service")
@Transactional
class CalendarTaskServiceImpl(
    private val calendarMapper: CalendarTaskMapper,
    private val calendarRepository: CalendarRepository
) : CalendarTaskService {

    override fun getCalendarMonth(year: Int, month: Int): List<List<CalendarDto>> {
        val maxDays = Month.of(month).length(Year.isLeap(year.toLong()))
        val dateInit = LocalDateTime.of(year, month, 1, 0, 0, 0)
        val dateFinish = LocalDateTime.of(year, month, maxDays, 23, 59, 59)

        val tasks = calendarRepository.findAllByScheduleInitAfterAndScheduleFinishBeforeOrderByScheduleInitAsc(dateInit, dateFinish)
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
                        this.totalDay = dayExtraMinus.toLocalDate()
                        this.tasks = mutableListOf()
                    }
                )
            }
        }

        for (day in 1..maxDays) {
            val dayTasks = tasks.filter { it.scheduleInit?.dayOfMonth == day }
            val actualDate = dateInit.plusDays((day - 1).toLong())

            finalList.add(
                CalendarDto().apply {
                    this.dayNumber = day
                    this.dayOfWeek = DayOfWeek.from(actualDate)
                    this.outOfMonth = false
                    this.totalDay = actualDate.toLocalDate()
                    this.tasks = dayTasks.map(calendarMapper::toDto)
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
                        this.totalDay = dayExtraPlus.toLocalDate()
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

    override fun getCalendarDay(year: Int, month: Int, day: Int): CalendarDto {
        val dateInit = LocalDateTime.of(year, month, day, 0, 0, 0)
        val dateFinish = LocalDateTime.of(year, month, day, 23, 59, 59)

        val tasks = calendarRepository.findAllByScheduleInitAfterAndScheduleFinishBeforeOrderByScheduleInitAsc(dateInit, dateFinish)
        return CalendarDto().apply {
            this.dayNumber = day
            this.dayOfWeek = DayOfWeek.from(dateInit)
            this.outOfMonth = false
            this.totalDay = dateInit.toLocalDate()
            this.tasks = tasks.map(calendarMapper::toDto)
        }
    }
}
