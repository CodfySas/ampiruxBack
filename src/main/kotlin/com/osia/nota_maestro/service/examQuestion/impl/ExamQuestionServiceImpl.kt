package com.osia.nota_maestro.service.examQuestion.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionMapper
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionRequest
import com.osia.nota_maestro.model.ExamQuestion
import com.osia.nota_maestro.repository.examQuestion.ExamQuestionRepository
import com.osia.nota_maestro.service.examQuestion.ExamQuestionService
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

@Service("examQuestion.crud_service")
@Transactional
class ExamQuestionServiceImpl(
    private val examQuestionRepository: ExamQuestionRepository,
    private val examQuestionMapper: ExamQuestionMapper,
    private val objectMapper: ObjectMapper
) : ExamQuestionService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("examQuestion count -> increment: $increment")
        return examQuestionRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ExamQuestion {
        return examQuestionRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ExamQuestion $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ExamQuestionDto> {
        log.trace("examQuestion findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return examQuestionRepository.findAllById(uuidList).map(examQuestionMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ExamQuestionDto> {
        log.trace("examQuestion findAll -> pageable: $pageable")
        return examQuestionRepository.findAll(Specification.where(CreateSpec<ExamQuestion>().createSpec("", school)), pageable).map(examQuestionMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamQuestionDto> {
        log.trace("examQuestion findAllByFilter -> pageable: $pageable, where: $where")
        return examQuestionRepository.findAll(Specification.where(CreateSpec<ExamQuestion>().createSpec(where, school)), pageable).map(examQuestionMapper::toDto)
    }

    @Transactional
    override fun save(examQuestionRequest: ExamQuestionRequest, replace: Boolean): ExamQuestionDto {
        log.trace("examQuestion save -> request: $examQuestionRequest")
        val savedExamQuestion = examQuestionMapper.toModel(examQuestionRequest)
        return examQuestionMapper.toDto(examQuestionRepository.save(savedExamQuestion))
    }

    @Transactional
    override fun saveMultiple(examQuestionRequestList: List<ExamQuestionRequest>): List<ExamQuestionDto> {
        log.trace("examQuestion saveMultiple -> requestList: ${objectMapper.writeValueAsString(examQuestionRequestList)}")
        val examQuestions = examQuestionRequestList.map(examQuestionMapper::toModel)
        return examQuestionRepository.saveAll(examQuestions).map(examQuestionMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, examQuestionRequest: ExamQuestionRequest, includeDelete: Boolean): ExamQuestionDto {
        log.trace("examQuestion update -> uuid: $uuid, request: $examQuestionRequest")
        val examQuestion = if (!includeDelete) {
            getById(uuid)
        } else {
            examQuestionRepository.getByUuid(uuid).get()
        }
        examQuestionMapper.update(examQuestionRequest, examQuestion)
        return examQuestionMapper.toDto(examQuestionRepository.save(examQuestion))
    }

    @Transactional
    override fun updateMultiple(examQuestionDtoList: List<ExamQuestionDto>): List<ExamQuestionDto> {
        log.trace("examQuestion updateMultiple -> examQuestionDtoList: ${objectMapper.writeValueAsString(examQuestionDtoList)}")
        val examQuestions = examQuestionRepository.findAllById(examQuestionDtoList.mapNotNull { it.uuid })
        examQuestions.forEach { examQuestion ->
            examQuestionMapper.update(examQuestionMapper.toRequest(examQuestionDtoList.first { it.uuid == examQuestion.uuid }), examQuestion)
        }
        return examQuestionRepository.saveAll(examQuestions).map(examQuestionMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("examQuestion delete -> uuid: $uuid")
        val examQuestion = getById(uuid)
        examQuestion.deleted = true
        examQuestion.deletedAt = LocalDateTime.now()
        examQuestionRepository.save(examQuestion)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("examQuestion deleteMultiple -> uuid: $uuidList")
        val examQuestions = examQuestionRepository.findAllById(uuidList)
        examQuestions.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        examQuestionRepository.saveAll(examQuestions)
    }
}
