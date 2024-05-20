package com.osia.nota_maestro.service.studentPosition.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionDto
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionMapper
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionRequest
import com.osia.nota_maestro.model.StudentPosition
import com.osia.nota_maestro.repository.studentPosition.StudentPositionRepository
import com.osia.nota_maestro.service.studentPosition.StudentPositionService
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

@Service("studentPosition.crud_service")
@Transactional
class StudentPositionServiceImpl(
    private val studentPositionRepository: StudentPositionRepository,
    private val studentPositionMapper: StudentPositionMapper,
    private val objectMapper: ObjectMapper
) : StudentPositionService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("studentPosition count -> increment: $increment")
        return studentPositionRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): StudentPosition {
        return studentPositionRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "StudentPosition $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<StudentPositionDto> {
        log.trace("studentPosition findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return studentPositionRepository.findAllById(uuidList).map(studentPositionMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<StudentPositionDto> {
        log.trace("studentPosition findAll -> pageable: $pageable")
        return studentPositionRepository.findAll(Specification.where(CreateSpec<StudentPosition>().createSpec("", school)), pageable).map(studentPositionMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentPositionDto> {
        log.trace("studentPosition findAllByFilter -> pageable: $pageable, where: $where")
        return studentPositionRepository.findAll(Specification.where(CreateSpec<StudentPosition>().createSpec(where, school)), pageable).map(studentPositionMapper::toDto)
    }

    @Transactional
    override fun save(studentPositionRequest: StudentPositionRequest, replace: Boolean): StudentPositionDto {
        log.trace("studentPosition save -> request: $studentPositionRequest")
        val savedStudentPosition = studentPositionMapper.toModel(studentPositionRequest)
        return studentPositionMapper.toDto(studentPositionRepository.save(savedStudentPosition))
    }

    @Transactional
    override fun saveMultiple(studentPositionRequestList: List<StudentPositionRequest>): List<StudentPositionDto> {
        log.trace("studentPosition saveMultiple -> requestList: ${objectMapper.writeValueAsString(studentPositionRequestList)}")
        val studentPositions = studentPositionRequestList.map(studentPositionMapper::toModel)
        return studentPositionRepository.saveAll(studentPositions).map(studentPositionMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, studentPositionRequest: StudentPositionRequest, includeDelete: Boolean): StudentPositionDto {
        log.trace("studentPosition update -> uuid: $uuid, request: $studentPositionRequest")
        val studentPosition = if (!includeDelete) {
            getById(uuid)
        } else {
            studentPositionRepository.getByUuid(uuid).get()
        }
        studentPositionMapper.update(studentPositionRequest, studentPosition)
        return studentPositionMapper.toDto(studentPositionRepository.save(studentPosition))
    }

    @Transactional
    override fun updateMultiple(studentPositionDtoList: List<StudentPositionDto>): List<StudentPositionDto> {
        log.trace("studentPosition updateMultiple -> studentPositionDtoList: ${objectMapper.writeValueAsString(studentPositionDtoList)}")
        val studentPositions = studentPositionRepository.findAllById(studentPositionDtoList.mapNotNull { it.uuid })
        studentPositions.forEach { studentPosition ->
            studentPositionMapper.update(studentPositionMapper.toRequest(studentPositionDtoList.first { it.uuid == studentPosition.uuid }), studentPosition)
        }
        return studentPositionRepository.saveAll(studentPositions).map(studentPositionMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("studentPosition delete -> uuid: $uuid")
        val studentPosition = getById(uuid)
        studentPosition.deleted = true
        studentPosition.deletedAt = LocalDateTime.now()
        studentPositionRepository.save(studentPosition)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("studentPosition deleteMultiple -> uuid: $uuidList")
        val studentPositions = studentPositionRepository.findAllById(uuidList)
        studentPositions.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        studentPositionRepository.saveAll(studentPositions)
    }
}
