package com.osia.nota_maestro.service.school.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.school.v1.SchoolDto
import com.osia.nota_maestro.dto.school.v1.SchoolMapper
import com.osia.nota_maestro.dto.school.v1.SchoolRequest
import com.osia.nota_maestro.model.School
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.service.school.SchoolService
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

@Service("school.crud_service")
@Transactional
class SchoolServiceImpl(
    private val schoolRepository: SchoolRepository,
    private val schoolMapper: SchoolMapper,
    private val objectMapper: ObjectMapper,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val schoolPeriodService: SchoolPeriodService
) : SchoolService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("school count -> increment: $increment")
        return schoolRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): School {
        return schoolRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "School $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<SchoolDto> {
        log.trace("school findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return schoolRepository.findAllById(uuidList).map(schoolMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<SchoolDto> {
        log.trace("school findAll -> pageable: $pageable")
        return schoolRepository.findAll(pageable).map(schoolMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<SchoolDto> {
        log.trace("school findAllByFilter -> pageable: $pageable, where: $where")
        return schoolRepository.findAll(Specification.where(CreateSpec<School>().createSpec(where)), pageable).map(schoolMapper::toDto)
    }

    @Transactional
    override fun save(schoolRequest: SchoolRequest): SchoolDto {
        log.trace("school save -> request: $schoolRequest")
        val school = schoolMapper.toModel(schoolRequest)
        return schoolMapper.toDto(schoolRepository.save(school))
    }

    @Transactional
    override fun saveMultiple(schoolRequestList: List<SchoolRequest>): List<SchoolDto> {
        log.trace("school saveMultiple -> requestList: ${objectMapper.writeValueAsString(schoolRequestList)}")
        val schools = schoolRequestList.map(schoolMapper::toModel)
        return schoolRepository.saveAll(schools).map(schoolMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, schoolRequest: SchoolRequest): SchoolDto {
        log.trace("school update -> uuid: $uuid, request: $schoolRequest")
        val school = getById(uuid)
        schoolMapper.update(schoolRequest, school)
        schoolPeriodRepository.deleteByUuidSchool(uuid)
        schoolRequest.periodList?.forEach {
            it.uuidSchool = uuid
        }
        schoolRequest.periodList?.let { schoolPeriodService.saveMultiple(it) }
        return schoolMapper.toDto(schoolRepository.save(school))
    }

    @Transactional
    override fun updateMultiple(schoolDtoList: List<SchoolDto>): List<SchoolDto> {
        log.trace("school updateMultiple -> schoolDtoList: ${objectMapper.writeValueAsString(schoolDtoList)}")
        val schools = schoolRepository.findAllById(schoolDtoList.mapNotNull { it.uuid })
        schools.forEach { school ->
            schoolMapper.update(schoolMapper.toRequest(schoolDtoList.first { it.uuid == school.uuid }), school)
        }
        return schoolRepository.saveAll(schools).map(schoolMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("school delete -> uuid: $uuid")
        val school = getById(uuid)
        school.deleted = true
        school.deletedAt = LocalDateTime.now()
        schoolRepository.save(school)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("school deleteMultiple -> uuid: $uuidList")
        val schools = schoolRepository.findAllById(uuidList)
        schools.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        schoolRepository.saveAll(schools)
    }
}
