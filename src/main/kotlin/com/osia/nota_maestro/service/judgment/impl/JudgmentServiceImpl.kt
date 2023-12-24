package com.osia.nota_maestro.service.judgment.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.judgment.v1.JudgmentDto
import com.osia.nota_maestro.dto.judgment.v1.JudgmentMapper
import com.osia.nota_maestro.dto.judgment.v1.JudgmentRequest
import com.osia.nota_maestro.model.Judgment
import com.osia.nota_maestro.repository.judgment.JudgmentRepository
import com.osia.nota_maestro.service.judgment.JudgmentService
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

@Service("judgment.crud_service")
@Transactional
class JudgmentServiceImpl(
    private val judgmentRepository: JudgmentRepository,
    private val judgmentMapper: JudgmentMapper,
    private val objectMapper: ObjectMapper
) : JudgmentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("judgment count -> increment: $increment")
        return judgmentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Judgment {
        return judgmentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Judgment $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<JudgmentDto> {
        log.trace("judgment findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return judgmentRepository.findAllById(uuidList).map(judgmentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<JudgmentDto> {
        log.trace("judgment findAll -> pageable: $pageable")
        return judgmentRepository.findAll(Specification.where(CreateSpec<Judgment>().createSpec("", school)), pageable).map(judgmentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<JudgmentDto> {
        log.trace("judgment findAllByFilter -> pageable: $pageable, where: $where")
        return judgmentRepository.findAll(Specification.where(CreateSpec<Judgment>().createSpec(where, school)), pageable).map(judgmentMapper::toDto)
    }

    @Transactional
    override fun save(judgmentRequest: JudgmentRequest, replace: Boolean): JudgmentDto {
        log.trace("judgment save -> request: $judgmentRequest")
        val savedJudgment = judgmentMapper.toModel(judgmentRequest)
        return judgmentMapper.toDto(judgmentRepository.save(savedJudgment))
    }

    @Transactional
    override fun saveMultiple(judgmentRequestList: List<JudgmentRequest>): List<JudgmentDto> {
        log.trace("judgment saveMultiple -> requestList: ${objectMapper.writeValueAsString(judgmentRequestList)}")
        val judgments = judgmentRequestList.map(judgmentMapper::toModel)
        return judgmentRepository.saveAll(judgments).map(judgmentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, judgmentRequest: JudgmentRequest, includeDelete: Boolean): JudgmentDto {
        log.trace("judgment update -> uuid: $uuid, request: $judgmentRequest")
        val judgment = if (!includeDelete) {
            getById(uuid)
        } else {
            judgmentRepository.getByUuid(uuid).get()
        }
        judgmentMapper.update(judgmentRequest, judgment)
        return judgmentMapper.toDto(judgmentRepository.save(judgment))
    }

    @Transactional
    override fun updateMultiple(judgmentDtoList: List<JudgmentDto>): List<JudgmentDto> {
        log.trace("judgment updateMultiple -> judgmentDtoList: ${objectMapper.writeValueAsString(judgmentDtoList)}")
        val judgments = judgmentRepository.findAllById(judgmentDtoList.mapNotNull { it.uuid })
        judgments.forEach { judgment ->
            judgmentMapper.update(judgmentMapper.toRequest(judgmentDtoList.first { it.uuid == judgment.uuid }), judgment)
        }
        return judgmentRepository.saveAll(judgments).map(judgmentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("judgment delete -> uuid: $uuid")
        val judgment = getById(uuid)
        judgment.deleted = true
        judgment.deletedAt = LocalDateTime.now()
        judgmentRepository.save(judgment)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("judgment deleteMultiple -> uuid: $uuidList")
        val judgments = judgmentRepository.findAllById(uuidList)
        judgments.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        judgmentRepository.saveAll(judgments)
    }
}
