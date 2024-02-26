package com.osia.nota_maestro.service.attendanceFail.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailDto
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailMapper
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailRequest
import com.osia.nota_maestro.model.AttendanceFail
import com.osia.nota_maestro.repository.attendanceFail.AttendanceFailRepository
import com.osia.nota_maestro.service.attendanceFail.AttendanceFailService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID

@Service("attendanceFail.crud_service")
@Transactional
class AttendanceFailServiceImpl(
    private val attendanceFailRepository: AttendanceFailRepository,
    private val attendanceFailMapper: AttendanceFailMapper,
    private val objectMapper: ObjectMapper
) : AttendanceFailService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("attendanceFail count -> increment: $increment")
        return attendanceFailRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): AttendanceFail {
        return attendanceFailRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "AttendanceFail $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<AttendanceFailDto> {
        log.trace("attendanceFail findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return attendanceFailRepository.findAllById(uuidList).map(attendanceFailMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<AttendanceFailDto> {
        log.trace("attendanceFail findAll -> pageable: $pageable")
        return attendanceFailRepository.findAll(Specification.where(CreateSpec<AttendanceFail>().createSpec("", school)), pageable).map(attendanceFailMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AttendanceFailDto> {
        log.trace("attendanceFail findAllByFilter -> pageable: $pageable, where: $where")
        return attendanceFailRepository.findAll(Specification.where(CreateSpec<AttendanceFail>().createSpec(where, school)), pageable).map(attendanceFailMapper::toDto)
    }

    @Transactional
    override fun save(attendanceFailRequest: AttendanceFailRequest, replace: Boolean): AttendanceFailDto {
        log.trace("attendanceFail save -> request: $attendanceFailRequest")
        val savedAttendanceFail = attendanceFailMapper.toModel(attendanceFailRequest)
        return attendanceFailMapper.toDto(attendanceFailRepository.save(savedAttendanceFail))
    }

    @Transactional
    override fun saveMultiple(attendanceFailRequestList: List<AttendanceFailRequest>): List<AttendanceFailDto> {
        log.trace("attendanceFail saveMultiple -> requestList: ${objectMapper.writeValueAsString(attendanceFailRequestList)}")
        val attendanceFails = attendanceFailRequestList.map(attendanceFailMapper::toModel)
        return attendanceFailRepository.saveAll(attendanceFails).map(attendanceFailMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, attendanceFailRequest: AttendanceFailRequest, includeDelete: Boolean): AttendanceFailDto {
        log.trace("attendanceFail update -> uuid: $uuid, request: $attendanceFailRequest")
        val attendanceFail = if (!includeDelete) {
            getById(uuid)
        } else {
            attendanceFailRepository.getByUuid(uuid).get()
        }
        attendanceFailMapper.update(attendanceFailRequest, attendanceFail)
        return attendanceFailMapper.toDto(attendanceFailRepository.save(attendanceFail))
    }

    @Transactional
    override fun updateMultiple(attendanceFailDtoList: List<AttendanceFailDto>): List<AttendanceFailDto> {
        log.trace("attendanceFail updateMultiple -> attendanceFailDtoList: ${objectMapper.writeValueAsString(attendanceFailDtoList)}")
        val attendanceFails = attendanceFailRepository.findAllById(attendanceFailDtoList.mapNotNull { it.uuid })
        attendanceFails.forEach { attendanceFail ->
            attendanceFailMapper.update(attendanceFailMapper.toRequest(attendanceFailDtoList.first { it.uuid == attendanceFail.uuid }), attendanceFail)
        }
        return attendanceFailRepository.saveAll(attendanceFails).map(attendanceFailMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("attendanceFail delete -> uuid: $uuid")
        val attendanceFail = getById(uuid)
        attendanceFail.deleted = true
        attendanceFail.deletedAt = LocalDateTime.now()
        attendanceFailRepository.save(attendanceFail)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("attendanceFail deleteMultiple -> uuid: $uuidList")
        val attendanceFails = attendanceFailRepository.findAllById(uuidList)
        attendanceFails.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        attendanceFailRepository.saveAll(attendanceFails)
    }
}
