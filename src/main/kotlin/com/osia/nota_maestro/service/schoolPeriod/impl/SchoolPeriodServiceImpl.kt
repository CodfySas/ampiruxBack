package com.osia.nota_maestro.service.schoolPeriod.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodDto
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodMapper
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodRequest
import com.osia.nota_maestro.model.SchoolPeriod
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.service.schoolPeriod.SchoolPeriodService
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

@Service("schoolPeriod.crud_service")
@Transactional
class SchoolPeriodServiceImpl(
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val schoolPeriodMapper: SchoolPeriodMapper,
    private val objectMapper: ObjectMapper
) : SchoolPeriodService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("schoolPeriod count -> increment: $increment")
        return schoolPeriodRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): SchoolPeriod {
        return schoolPeriodRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "SchoolPeriod $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<SchoolPeriodDto> {
        log.trace("schoolPeriod findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return schoolPeriodRepository.findAllById(uuidList).map(schoolPeriodMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<SchoolPeriodDto> {
        log.trace("schoolPeriod findAll -> pageable: $pageable")
        return schoolPeriodRepository.findAll(Specification.where(CreateSpec<SchoolPeriod>().createSpec("", school)), pageable).map(schoolPeriodMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SchoolPeriodDto> {
        log.trace("schoolPeriod findAllByFilter -> pageable: $pageable, where: $where")
        return schoolPeriodRepository.findAll(Specification.where(CreateSpec<SchoolPeriod>().createSpec(where, school)), pageable).map(schoolPeriodMapper::toDto)
    }

    @Transactional
    override fun save(schoolPeriodRequest: SchoolPeriodRequest, replace: Boolean): SchoolPeriodDto {
        log.trace("schoolPeriod save -> request: $schoolPeriodRequest")
        val savedSchoolPeriod = schoolPeriodMapper.toModel(schoolPeriodRequest)
        return schoolPeriodMapper.toDto(schoolPeriodRepository.save(savedSchoolPeriod))
    }

    @Transactional
    override fun saveMultiple(schoolPeriodRequestList: List<SchoolPeriodRequest>): List<SchoolPeriodDto> {
        log.trace("schoolPeriod saveMultiple -> requestList: ${objectMapper.writeValueAsString(schoolPeriodRequestList)}")
        val schoolPeriods = schoolPeriodRequestList.map(schoolPeriodMapper::toModel)
        return schoolPeriodRepository.saveAll(schoolPeriods).map(schoolPeriodMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, schoolPeriodRequest: SchoolPeriodRequest, includeDelete: Boolean): SchoolPeriodDto {
        log.trace("schoolPeriod update -> uuid: $uuid, request: $schoolPeriodRequest")
        val schoolPeriod = if (!includeDelete) {
            getById(uuid)
        } else {
            schoolPeriodRepository.getByUuid(uuid).get()
        }
        schoolPeriodMapper.update(schoolPeriodRequest, schoolPeriod)
        return schoolPeriodMapper.toDto(schoolPeriodRepository.save(schoolPeriod))
    }

    @Transactional
    override fun updateMultiple(schoolPeriodDtoList: List<SchoolPeriodDto>): List<SchoolPeriodDto> {
        log.trace("schoolPeriod updateMultiple -> schoolPeriodDtoList: ${objectMapper.writeValueAsString(schoolPeriodDtoList)}")
        val schoolPeriods = schoolPeriodRepository.findAllById(schoolPeriodDtoList.mapNotNull { it.uuid })
        schoolPeriods.forEach { schoolPeriod ->
            schoolPeriodMapper.update(schoolPeriodMapper.toRequest(schoolPeriodDtoList.first { it.uuid == schoolPeriod.uuid }), schoolPeriod)
        }
        return schoolPeriodRepository.saveAll(schoolPeriods).map(schoolPeriodMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("schoolPeriod delete -> uuid: $uuid")
        val schoolPeriod = getById(uuid)
        schoolPeriod.deleted = true
        schoolPeriod.deletedAt = LocalDateTime.now()
        schoolPeriodRepository.save(schoolPeriod)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("schoolPeriod deleteMultiple -> uuid: $uuidList")
        val schoolPeriods = schoolPeriodRepository.findAllById(uuidList)
        schoolPeriods.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        schoolPeriodRepository.saveAll(schoolPeriods)
    }
}
