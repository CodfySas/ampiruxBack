package com.osia.nota_maestro.service.examUserResponse.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseDto
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseMapper
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseRequest
import com.osia.nota_maestro.model.ExamUserResponse
import com.osia.nota_maestro.repository.examUserResponse.ExamUserResponseRepository
import com.osia.nota_maestro.service.examUserResponse.ExamUserResponseService
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

@Service("examUserResponse.crud_service")
@Transactional
class ExamUserResponseServiceImpl(
    private val examUserResponseRepository: ExamUserResponseRepository,
    private val examUserResponseMapper: ExamUserResponseMapper,
    private val objectMapper: ObjectMapper
) : ExamUserResponseService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("examUserResponse count -> increment: $increment")
        return examUserResponseRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ExamUserResponse {
        return examUserResponseRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ExamUserResponse $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ExamUserResponseDto> {
        log.trace("examUserResponse findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return examUserResponseRepository.findAllById(uuidList).map(examUserResponseMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ExamUserResponseDto> {
        log.trace("examUserResponse findAll -> pageable: $pageable")
        return examUserResponseRepository.findAll(Specification.where(CreateSpec<ExamUserResponse>().createSpec("", school)), pageable).map(examUserResponseMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamUserResponseDto> {
        log.trace("examUserResponse findAllByFilter -> pageable: $pageable, where: $where")
        return examUserResponseRepository.findAll(Specification.where(CreateSpec<ExamUserResponse>().createSpec(where, school)), pageable).map(examUserResponseMapper::toDto)
    }

    @Transactional
    override fun save(examUserResponseRequest: ExamUserResponseRequest, replace: Boolean): ExamUserResponseDto {
        log.trace("examUserResponse save -> request: $examUserResponseRequest")
        val savedExamUserResponse = examUserResponseMapper.toModel(examUserResponseRequest)
        return examUserResponseMapper.toDto(examUserResponseRepository.save(savedExamUserResponse))
    }

    @Transactional
    override fun saveMultiple(examUserResponseRequestList: List<ExamUserResponseRequest>): List<ExamUserResponseDto> {
        log.trace("examUserResponse saveMultiple -> requestList: ${objectMapper.writeValueAsString(examUserResponseRequestList)}")
        val examUserResponses = examUserResponseRequestList.map(examUserResponseMapper::toModel)
        return examUserResponseRepository.saveAll(examUserResponses).map(examUserResponseMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, examUserResponseRequest: ExamUserResponseRequest, includeDelete: Boolean): ExamUserResponseDto {
        log.trace("examUserResponse update -> uuid: $uuid, request: $examUserResponseRequest")
        val examUserResponse = if (!includeDelete) {
            getById(uuid)
        } else {
            examUserResponseRepository.getByUuid(uuid).get()
        }
        examUserResponseMapper.update(examUserResponseRequest, examUserResponse)
        return examUserResponseMapper.toDto(examUserResponseRepository.save(examUserResponse))
    }

    @Transactional
    override fun updateMultiple(examUserResponseDtoList: List<ExamUserResponseDto>): List<ExamUserResponseDto> {
        log.trace("examUserResponse updateMultiple -> examUserResponseDtoList: ${objectMapper.writeValueAsString(examUserResponseDtoList)}")
        val examUserResponses = examUserResponseRepository.findAllById(examUserResponseDtoList.mapNotNull { it.uuid })
        examUserResponses.forEach { examUserResponse ->
            examUserResponseMapper.update(examUserResponseMapper.toRequest(examUserResponseDtoList.first { it.uuid == examUserResponse.uuid }), examUserResponse)
        }
        return examUserResponseRepository.saveAll(examUserResponses).map(examUserResponseMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("examUserResponse delete -> uuid: $uuid")
        val examUserResponse = getById(uuid)
        examUserResponse.deleted = true
        examUserResponse.deletedAt = LocalDateTime.now()
        examUserResponseRepository.save(examUserResponse)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("examUserResponse deleteMultiple -> uuid: $uuidList")
        val examUserResponses = examUserResponseRepository.findAllById(uuidList)
        examUserResponses.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        examUserResponseRepository.saveAll(examUserResponses)
    }
}
