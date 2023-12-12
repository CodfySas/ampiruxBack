package com.osia.nota_maestro.service.gradeSubject.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectDto
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectMapper
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectRequest
import com.osia.nota_maestro.model.GradeSubject
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.service.gradeSubject.GradeSubjectService
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

@Service("gradeSubject.crud_service")
@Transactional
class GradeSubjectServiceImpl(
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val gradeSubjectMapper: GradeSubjectMapper,
    private val objectMapper: ObjectMapper
) : GradeSubjectService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("gradeSubject count -> increment: $increment")
        return gradeSubjectRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): GradeSubject {
        return gradeSubjectRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "GradeSubject $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<GradeSubjectDto> {
        log.trace("gradeSubject findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return gradeSubjectRepository.findAllById(uuidList).map(gradeSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<GradeSubjectDto> {
        log.trace("gradeSubject findAll -> pageable: $pageable")
        return gradeSubjectRepository.findAll(Specification.where(CreateSpec<GradeSubject>().createSpec("", school)), pageable).map(gradeSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<GradeSubjectDto> {
        log.trace("gradeSubject findAllByFilter -> pageable: $pageable, where: $where")
        return gradeSubjectRepository.findAll(Specification.where(CreateSpec<GradeSubject>().createSpec(where, school)), pageable).map(gradeSubjectMapper::toDto)
    }

    @Transactional
    override fun save(gradeSubjectRequest: GradeSubjectRequest, replace: Boolean): GradeSubjectDto {
        log.trace("gradeSubject save -> request: $gradeSubjectRequest")
        return gradeSubjectMapper.toDto(gradeSubjectRepository.save(gradeSubjectMapper.toModel(gradeSubjectRequest)))
    }

    @Transactional
    override fun saveMultiple(gradeSubjectRequestList: List<GradeSubjectRequest>): List<GradeSubjectDto> {
        log.trace("gradeSubject saveMultiple -> requestList: ${objectMapper.writeValueAsString(gradeSubjectRequestList)}")
        val gradeSubjects = gradeSubjectRequestList.map(gradeSubjectMapper::toModel)
        return gradeSubjectRepository.saveAll(gradeSubjects).map(gradeSubjectMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, gradeSubjectRequest: GradeSubjectRequest, includeDelete: Boolean): GradeSubjectDto {
        log.trace("gradeSubject update -> uuid: $uuid, request: $gradeSubjectRequest")
        val gradeSubject = if (!includeDelete) {
            getById(uuid)
        } else {
            gradeSubjectRepository.getByUuid(uuid).get()
        }
        gradeSubjectMapper.update(gradeSubjectRequest, gradeSubject)
        return gradeSubjectMapper.toDto(gradeSubjectRepository.save(gradeSubject))
    }

    @Transactional
    override fun updateMultiple(gradeSubjectDtoList: List<GradeSubjectDto>): List<GradeSubjectDto> {
        log.trace("gradeSubject updateMultiple -> gradeSubjectDtoList: ${objectMapper.writeValueAsString(gradeSubjectDtoList)}")
        val gradeSubjects = gradeSubjectRepository.findAllById(gradeSubjectDtoList.mapNotNull { it.uuid })
        gradeSubjects.forEach { gradeSubject ->
            gradeSubjectMapper.update(gradeSubjectMapper.toRequest(gradeSubjectDtoList.first { it.uuid == gradeSubject.uuid }), gradeSubject)
        }
        return gradeSubjectRepository.saveAll(gradeSubjects).map(gradeSubjectMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("gradeSubject delete -> uuid: $uuid")
        val gradeSubject = getById(uuid)
        gradeSubject.deleted = true
        gradeSubject.deletedAt = LocalDateTime.now()
        gradeSubjectRepository.save(gradeSubject)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("gradeSubject deleteMultiple -> uuid: $uuidList")
        val gradeSubjects = gradeSubjectRepository.findAllById(uuidList)
        gradeSubjects.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        gradeSubjectRepository.saveAll(gradeSubjects)
    }
}
