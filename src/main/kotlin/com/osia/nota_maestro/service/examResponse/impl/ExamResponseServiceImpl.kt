package com.osia.nota_maestro.service.examResponse.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseMapper
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseRequest
import com.osia.nota_maestro.model.ExamResponse
import com.osia.nota_maestro.repository.examResponse.ExamResponseRepository
import com.osia.nota_maestro.service.examResponse.ExamResponseService
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

@Service("examResponse.crud_service")
@Transactional
class ExamResponseServiceImpl(
    private val examResponseRepository: ExamResponseRepository,
    private val examResponseMapper: ExamResponseMapper,
    private val objectMapper: ObjectMapper
) : ExamResponseService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("examResponse count -> increment: $increment")
        return examResponseRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ExamResponse {
        return examResponseRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ExamResponse $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ExamResponseDto> {
        log.trace("examResponse findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return examResponseRepository.findAllById(uuidList).map(examResponseMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ExamResponseDto> {
        log.trace("examResponse findAll -> pageable: $pageable")
        return examResponseRepository.findAll(Specification.where(CreateSpec<ExamResponse>().createSpec("", school)), pageable).map(examResponseMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamResponseDto> {
        log.trace("examResponse findAllByFilter -> pageable: $pageable, where: $where")
        return examResponseRepository.findAll(Specification.where(CreateSpec<ExamResponse>().createSpec(where, school)), pageable).map(examResponseMapper::toDto)
    }

    @Transactional
    override fun save(examResponseRequest: ExamResponseRequest, replace: Boolean): ExamResponseDto {
        log.trace("examResponse save -> request: $examResponseRequest")
        val savedExamResponse = examResponseMapper.toModel(examResponseRequest)
        return examResponseMapper.toDto(examResponseRepository.save(savedExamResponse))
    }

    @Transactional
    override fun saveMultiple(examResponseRequestList: List<ExamResponseRequest>): List<ExamResponseDto> {
        log.trace("examResponse saveMultiple -> requestList: ${objectMapper.writeValueAsString(examResponseRequestList)}")
        val examResponses = examResponseRequestList.map(examResponseMapper::toModel)
        return examResponseRepository.saveAll(examResponses).map(examResponseMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, examResponseRequest: ExamResponseRequest, includeDelete: Boolean): ExamResponseDto {
        log.trace("examResponse update -> uuid: $uuid, request: $examResponseRequest")
        val examResponse = if (!includeDelete) {
            getById(uuid)
        } else {
            examResponseRepository.getByUuid(uuid).get()
        }
        examResponseMapper.update(examResponseRequest, examResponse)
        return examResponseMapper.toDto(examResponseRepository.save(examResponse))
    }

    @Transactional
    override fun updateMultiple(examResponseDtoList: List<ExamResponseDto>): List<ExamResponseDto> {
        log.trace("examResponse updateMultiple -> examResponseDtoList: ${objectMapper.writeValueAsString(examResponseDtoList)}")
        val examResponses = examResponseRepository.findAllById(examResponseDtoList.mapNotNull { it.uuid })
        examResponses.forEach { examResponse ->
            examResponseMapper.update(examResponseMapper.toRequest(examResponseDtoList.first { it.uuid == examResponse.uuid }), examResponse)
        }
        return examResponseRepository.saveAll(examResponses).map(examResponseMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("examResponse delete -> uuid: $uuid")
        val examResponse = getById(uuid)
        examResponse.deleted = true
        examResponse.deletedAt = LocalDateTime.now()
        examResponseRepository.save(examResponse)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("examResponse deleteMultiple -> uuid: $uuidList")
        val examResponses = examResponseRepository.findAllById(uuidList)
        examResponses.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        examResponseRepository.saveAll(examResponses)
    }
}
