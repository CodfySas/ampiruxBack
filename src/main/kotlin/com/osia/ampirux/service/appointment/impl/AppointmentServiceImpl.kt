package com.osia.ampirux.service.appointment.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.appointment.v1.AppointmentDto
import com.osia.ampirux.dto.appointment.v1.AppointmentRequest
import com.osia.ampirux.dto.appointment.v1.CalendarDto
import com.osia.ampirux.model.Appointment
import com.osia.ampirux.repository.appointment.AppointmentRepository
import com.osia.ampirux.service.appointment.AppointmentService
import com.osia.ampirux.service.client.ClientService
import com.osia.ampirux.service.service.ServiceService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class AppointmentServiceImpl(
    private val repository: AppointmentRepository,
    private val mapper: BaseMapper<AppointmentRequest, Appointment, AppointmentDto>,
    private val objectMapper: ObjectMapper,
    private val clientService: ClientService,
    private val serviceService: ServiceService,
) : AppointmentService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Appointment {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<AppointmentDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<AppointmentDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<Appointment>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<AppointmentDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<Appointment>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: AppointmentRequest, replace: Boolean): AppointmentDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun saveMultiple(requestList: List<AppointmentRequest>): List<AppointmentDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: AppointmentRequest, includeDelete: Boolean): AppointmentDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun updateMultiple(dtoList: List<AppointmentDto>): List<AppointmentDto> {
        log.trace("updateMultiple -> dtoList: ${objectMapper.writeValueAsString(dtoList)}")
        val ids = dtoList.mapNotNull { it.uuid }
        val entities = repository.findAllById(ids)
        entities.forEach { entity ->
            val dto = dtoList.first { it.uuid == entity.uuid }
            mapper.update(mapper.toRequest(dto), entity)
        }
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun delete(id: UUID) {
        log.trace("delete -> id: $id")
        val entity = getById(id)
        entity.deleted = true
        entity.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        repository.save(entity)
    }

    @Transactional
    override fun deleteMultiple(idList: List<UUID>) {
        log.trace("deleteMultiple -> idList: $idList")
        val entities = repository.findAllById(idList)
        entities.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        }
        repository.saveAll(entities)
    }

    override fun getCalendarMonth(year: Int, month: Int, school: UUID): List<List<CalendarDto>> {
        val maxDays = Month.of(month).length(Year.isLeap(year.toLong()))
        val startOfMonth = LocalDate.of(year, month, 1).atStartOfDay()
        val endOfMonth = LocalDate.of(year, month, maxDays).atTime(23, 59, 59)

        val tasks = repository.findAllByScheduledAtBetweenAndBarbershopUuidOrderByScheduledAt(startOfMonth, endOfMonth, school).map(mapper::toDto)
        val clients = clientService.findByMultiple(tasks.mapNotNull { it.clientUuid }.distinct())
        val services = serviceService.findByMultiple(tasks.mapNotNull { it.serviceUuid }.distinct())
        tasks.forEach {
            it.service = services.firstOrNull { s-> s.uuid == it.serviceUuid }
            it.client = clients.firstOrNull { s-> s.uuid == it.clientUuid }
        }
        val finalList = mutableListOf<CalendarDto>()

        if (startOfMonth.dayOfWeek != DayOfWeek.MONDAY) {
            val initDayWeek = startOfMonth.dayOfWeek.value - 1
            repeat(initDayWeek) {
                val dayExtraMinus = startOfMonth.minusDays((initDayWeek - it).toLong())
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
            val dayTasks = tasks.filter { it.scheduledAt?.dayOfMonth == day }.sortedBy { it.scheduledAt }
            dayTasks.forEach {
                /*it.name = resources.firstOrNull {
                        resource ->
                    resource.uuid == it.uuidResource
                }?.name*/
            }
            val actualDate = startOfMonth.plusDays((day - 1).toLong())

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

        if (endOfMonth.dayOfWeek != DayOfWeek.SUNDAY) {
            val daysToAdd = 7 - (endOfMonth.dayOfWeek.value)
            repeat(daysToAdd) {
                val dayExtraPlus = endOfMonth.plusDays(it.toLong() + 1)
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
        // Crear la fecha específica usando los parámetros
        val specificDate = LocalDate.of(year, month, day)
        val startOfDay = specificDate.atStartOfDay()
        val endOfDay = specificDate.atTime(23, 59, 59)

        val tasks = repository.findAllByScheduledAtBetweenAndBarbershopUuidOrderByScheduledAt(startOfDay, endOfDay, school).map(mapper::toDto)
        val clients = clientService.findByMultiple(tasks.mapNotNull { it.clientUuid }.distinct())
        val services = serviceService.findByMultiple(tasks.mapNotNull { it.serviceUuid }.distinct())
        tasks.forEach {
            it.service = services.firstOrNull { s-> s.uuid == it.serviceUuid }
            it.client = clients.firstOrNull { s-> s.uuid == it.clientUuid }
        }
        return CalendarDto().apply {
            this.dayNumber = day
            this.dayOfWeek = DayOfWeek.from(startOfDay)
            this.outOfMonth = false
            this.totalDay = endOfDay
            this.tasks = tasks
        }
    }
}
