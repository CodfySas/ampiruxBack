package com.osia.nota_maestro.service.log.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.log.v1.LogDto
import com.osia.nota_maestro.dto.log.v1.LogMapper
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.model.Log
import com.osia.nota_maestro.repository.log.LogRepository
import com.osia.nota_maestro.service.log.LogService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.user.UserService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.UUID

@Service("log.crud_service")
@Transactional
class LogServiceImpl(
    private val logRepository: LogRepository,
    private val logMapper: LogMapper,
    private val objectMapper: ObjectMapper,
    private val schoolService: SchoolService,
    private val userService: UserService
) : LogService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("log count -> increment: $increment")
        return logRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Log {
        return logRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Log $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<LogDto> {
        log.trace("log findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return logRepository.findAllById(uuidList).map(logMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<LogDto> {
        log.trace("log findAll -> pageable: $pageable")
        return logRepository.findAll(Specification.where(CreateSpec<Log>().createSpec("", school)), pageable).map(logMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<LogDto> {
        log.trace("log findAllByFilter -> pageable: $pageable, where: $where")
        return logRepository.findAll(Specification.where(CreateSpec<Log>().createSpec(where, school)), pageable).map(logMapper::toDto)
    }

    @Transactional
    override fun save(logRequest: LogRequest, replace: Boolean): LogDto {
        log.trace("log save -> request: $logRequest")
        val savedLog = logMapper.toModel(logRequest)
        return logMapper.toDto(logRepository.save(savedLog))
    }

    @Transactional
    override fun saveMultiple(logRequestList: List<LogRequest>): List<LogDto> {
        log.trace("log saveMultiple -> requestList: ${objectMapper.writeValueAsString(logRequestList)}")
        val logs = logRequestList.map(logMapper::toModel)
        return logRepository.saveAll(logs).map(logMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, logRequest: LogRequest, includeDelete: Boolean): LogDto {
        log.trace("log update -> uuid: $uuid, request: $logRequest")
        val log = if (!includeDelete) {
            getById(uuid)
        } else {
            logRepository.getByUuid(uuid).get()
        }
        logMapper.update(logRequest, log)
        return logMapper.toDto(logRepository.save(log))
    }

    @Transactional
    override fun updateMultiple(logDtoList: List<LogDto>): List<LogDto> {
        log.trace("log updateMultiple -> logDtoList: ${objectMapper.writeValueAsString(logDtoList)}")
        val logs = logRepository.findAllById(logDtoList.mapNotNull { it.uuid })
        logs.forEach { log ->
            logMapper.update(logMapper.toRequest(logDtoList.first { it.uuid == log.uuid }), log)
        }
        return logRepository.saveAll(logs).map(logMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("log delete -> uuid: $uuid")
        val log = getById(uuid)
        log.deleted = true
        log.deletedAt = LocalDateTime.now()
        logRepository.save(log)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("log deleteMultiple -> uuid: $uuidList")
        val logs = logRepository.findAllById(uuidList)
        logs.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        logRepository.saveAll(logs)
    }

    override fun findAllByMonth(month: Int, day: Int, school: UUID): List<LogDto> {
        log.trace("log findAllByMonth -> month: $month, day: $day")
        val schoolF = schoolService.getById(school)
        val firstDay = LocalDate.of(schoolF.actualYear!!, month + 1, 1)
        val yearMonth = YearMonth.of(schoolF.actualYear!!, month + 1)
        val lastDay = yearMonth.atEndOfMonth()

        val founds = if (day != 0) {
            logRepository.findAllByDay(LocalDate.of(schoolF.actualYear!!, month + 1, day))
        } else {
            logRepository.findAllByDayBetween(firstDay, lastDay)
        }
        val users = userService.findByMultiple(founds.mapNotNull { it.uuidUser })
        val logs = founds.map(logMapper::toDto).sortedByDescending { it.createdAt }
        logs.forEach {
            val userF = users.firstOrNull { u -> u.uuid == it.uuidUser }
            it.userName = userF?.name + " " + userF?.lastname
            it.userCode = userF?.code
            it.userRole = userF?.role
        }
        return logs
    }
}
