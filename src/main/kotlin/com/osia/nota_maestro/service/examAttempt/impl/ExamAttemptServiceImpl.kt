package com.osia.nota_maestro.service.examAttempt.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptDto
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptMapper
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptRequest
import com.osia.nota_maestro.model.ExamAttempt
import com.osia.nota_maestro.repository.examAttempt.ExamAttemptRepository
import com.osia.nota_maestro.service.examAttempt.ExamAttemptService
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

@Service("examAttempt.crud_service")
@Transactional
class ExamAttemptServiceImpl(
    private val examAttemptRepository: ExamAttemptRepository,
    private val examAttemptMapper: ExamAttemptMapper,
    private val objectMapper: ObjectMapper
) : ExamAttemptService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("examAttempt count -> increment: $increment")
        return examAttemptRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ExamAttempt {
        return examAttemptRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ExamAttempt $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ExamAttemptDto> {
        log.trace("examAttempt findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return examAttemptRepository.findAllById(uuidList).map(examAttemptMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ExamAttemptDto> {
        log.trace("examAttempt findAll -> pageable: $pageable")
        return examAttemptRepository.findAll(Specification.where(CreateSpec<ExamAttempt>().createSpec("", school)), pageable).map(examAttemptMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamAttemptDto> {
        log.trace("examAttempt findAllByFilter -> pageable: $pageable, where: $where")
        return examAttemptRepository.findAll(Specification.where(CreateSpec<ExamAttempt>().createSpec(where, school)), pageable).map(examAttemptMapper::toDto)
    }

    @Transactional
    override fun save(examAttemptRequest: ExamAttemptRequest, replace: Boolean): ExamAttemptDto {
        log.trace("examAttempt save -> request: $examAttemptRequest")
        val savedExamAttempt = examAttemptMapper.toModel(examAttemptRequest)
        return examAttemptMapper.toDto(examAttemptRepository.save(savedExamAttempt))
    }

    @Transactional
    override fun saveMultiple(examAttemptRequestList: List<ExamAttemptRequest>): List<ExamAttemptDto> {
        log.trace("examAttempt saveMultiple -> requestList: ${objectMapper.writeValueAsString(examAttemptRequestList)}")
        val examAttempts = examAttemptRequestList.map(examAttemptMapper::toModel)
        return examAttemptRepository.saveAll(examAttempts).map(examAttemptMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, examAttemptRequest: ExamAttemptRequest, includeDelete: Boolean): ExamAttemptDto {
        log.trace("examAttempt update -> uuid: $uuid, request: $examAttemptRequest")
        val examAttempt = if (!includeDelete) {
            getById(uuid)
        } else {
            examAttemptRepository.getByUuid(uuid).get()
        }
        examAttemptMapper.update(examAttemptRequest, examAttempt)
        return examAttemptMapper.toDto(examAttemptRepository.save(examAttempt))
    }

    @Transactional
    override fun updateMultiple(examAttemptDtoList: List<ExamAttemptDto>): List<ExamAttemptDto> {
        log.trace("examAttempt updateMultiple -> examAttemptDtoList: ${objectMapper.writeValueAsString(examAttemptDtoList)}")
        val examAttempts = examAttemptRepository.findAllById(examAttemptDtoList.mapNotNull { it.uuid })
        examAttempts.forEach { examAttempt ->
            examAttemptMapper.update(examAttemptMapper.toRequest(examAttemptDtoList.first { it.uuid == examAttempt.uuid }), examAttempt)
        }
        return examAttemptRepository.saveAll(examAttempts).map(examAttemptMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("examAttempt delete -> uuid: $uuid")
        val examAttempt = getById(uuid)
        examAttempt.deleted = true
        examAttempt.deletedAt = LocalDateTime.now()
        examAttemptRepository.save(examAttempt)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("examAttempt deleteMultiple -> uuid: $uuidList")
        val examAttempts = examAttemptRepository.findAllById(uuidList)
        examAttempts.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        examAttemptRepository.saveAll(examAttempts)
    }
}
