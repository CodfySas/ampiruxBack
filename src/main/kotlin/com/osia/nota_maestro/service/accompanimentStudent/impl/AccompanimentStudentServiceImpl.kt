package com.osia.nota_maestro.service.accompanimentStudent.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentMapper
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentRequest
import com.osia.nota_maestro.model.AccompanimentStudent
import com.osia.nota_maestro.repository.accompanimentStudent.AccompanimentStudentRepository
import com.osia.nota_maestro.service.accompanimentStudent.AccompanimentStudentService
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

@Service("accompanimentStudent.crud_service")
@Transactional
class AccompanimentStudentServiceImpl(
    private val accompanimentStudentRepository: AccompanimentStudentRepository,
    private val accompanimentStudentMapper: AccompanimentStudentMapper,
    private val objectMapper: ObjectMapper
) : AccompanimentStudentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("accompanimentStudent count -> increment: $increment")
        return accompanimentStudentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): AccompanimentStudent {
        return accompanimentStudentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "AccompanimentStudent $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<AccompanimentStudentDto> {
        log.trace("accompanimentStudent findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return accompanimentStudentRepository.findAllById(uuidList).map(accompanimentStudentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<AccompanimentStudentDto> {
        log.trace("accompanimentStudent findAll -> pageable: $pageable")
        return accompanimentStudentRepository.findAll(Specification.where(CreateSpec<AccompanimentStudent>().createSpec("", school)), pageable).map(accompanimentStudentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AccompanimentStudentDto> {
        log.trace("accompanimentStudent findAllByFilter -> pageable: $pageable, where: $where")
        return accompanimentStudentRepository.findAll(Specification.where(CreateSpec<AccompanimentStudent>().createSpec(where, school)), pageable).map(accompanimentStudentMapper::toDto)
    }

    @Transactional
    override fun save(accompanimentStudentRequest: AccompanimentStudentRequest, replace: Boolean): AccompanimentStudentDto {
        log.trace("accompanimentStudent save -> request: $accompanimentStudentRequest")
        val savedAccompanimentStudent = accompanimentStudentMapper.toModel(accompanimentStudentRequest)
        return accompanimentStudentMapper.toDto(accompanimentStudentRepository.save(savedAccompanimentStudent))
    }

    @Transactional
    override fun saveMultiple(accompanimentStudentRequestList: List<AccompanimentStudentRequest>): List<AccompanimentStudentDto> {
        log.trace("accompanimentStudent saveMultiple -> requestList: ${objectMapper.writeValueAsString(accompanimentStudentRequestList)}")
        val accompanimentStudents = accompanimentStudentRequestList.map(accompanimentStudentMapper::toModel)
        return accompanimentStudentRepository.saveAll(accompanimentStudents).map(accompanimentStudentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, accompanimentStudentRequest: AccompanimentStudentRequest, includeDelete: Boolean): AccompanimentStudentDto {
        log.trace("accompanimentStudent update -> uuid: $uuid, request: $accompanimentStudentRequest")
        val accompanimentStudent = if (!includeDelete) {
            getById(uuid)
        } else {
            accompanimentStudentRepository.getByUuid(uuid).get()
        }
        accompanimentStudentMapper.update(accompanimentStudentRequest, accompanimentStudent)
        return accompanimentStudentMapper.toDto(accompanimentStudentRepository.save(accompanimentStudent))
    }

    @Transactional
    override fun updateMultiple(accompanimentStudentDtoList: List<AccompanimentStudentDto>): List<AccompanimentStudentDto> {
        log.trace("accompanimentStudent updateMultiple -> accompanimentStudentDtoList: ${objectMapper.writeValueAsString(accompanimentStudentDtoList)}")
        val accompanimentStudents = accompanimentStudentRepository.findAllById(accompanimentStudentDtoList.mapNotNull { it.uuid })
        accompanimentStudents.forEach { accompanimentStudent ->
            accompanimentStudentMapper.update(accompanimentStudentMapper.toRequest(accompanimentStudentDtoList.first { it.uuid == accompanimentStudent.uuid }), accompanimentStudent)
        }
        return accompanimentStudentRepository.saveAll(accompanimentStudents).map(accompanimentStudentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("accompanimentStudent delete -> uuid: $uuid")
        val accompanimentStudent = getById(uuid)
        accompanimentStudent.deleted = true
        accompanimentStudent.deletedAt = LocalDateTime.now()
        accompanimentStudentRepository.save(accompanimentStudent)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("accompanimentStudent deleteMultiple -> uuid: $uuidList")
        val accompanimentStudents = accompanimentStudentRepository.findAllById(uuidList)
        accompanimentStudents.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        accompanimentStudentRepository.saveAll(accompanimentStudents)
    }
}
