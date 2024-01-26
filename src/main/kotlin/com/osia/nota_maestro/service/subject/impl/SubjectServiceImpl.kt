package com.osia.nota_maestro.service.subject.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.subject.v1.SubjectDto
import com.osia.nota_maestro.dto.subject.v1.SubjectMapper
import com.osia.nota_maestro.dto.subject.v1.SubjectRequest
import com.osia.nota_maestro.model.Subject
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.service.subject.SubjectService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID

@Service("subject.crud_service")
@Transactional
class SubjectServiceImpl(
    private val subjectRepository: SubjectRepository,
    private val subjectMapper: SubjectMapper,
    private val objectMapper: ObjectMapper
) : SubjectService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("subject count -> increment: $increment")
        return subjectRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Subject {
        return subjectRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Subject $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<SubjectDto> {
        log.trace("subject findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return subjectRepository.findAllById(uuidList).map(subjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<SubjectDto> {
        log.trace("subject findAll -> pageable: $pageable")
        val pages = subjectRepository.findAll(Specification.where(CreateSpec<Subject>().createSpec("", school)), pageable).map(subjectMapper::toDto)
        val map = mutableMapOf<UUID, List<SubjectDto>>()
        val new = mutableListOf<SubjectDto>()
        pages.content.forEach {
            if (it.uuidParent != null) {
                val list = map.getOrPut(it.uuidParent!!) { mutableListOf() }.toMutableList()
                list.add(it)
                map[it.uuidParent!!] = list
            } else {
                new.add(it)
            }
        }
        new.forEach {
            it.childs = map[it.uuid] ?: mutableListOf()
        }
        return PageImpl(new, pageable, pages.totalElements)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SubjectDto> {
        log.trace("subject findAllByFilter -> pageable: $pageable, where: $where")
        return subjectRepository.findAll(Specification.where(CreateSpec<Subject>().createSpec(where, school)), pageable).map(subjectMapper::toDto)
    }

    @Transactional
    override fun save(subjectRequest: SubjectRequest, school: UUID, replace: Boolean): SubjectDto {
        log.trace("subject save -> request: $subjectRequest")
        subjectRequest.uuidSchool = school
        val saved = subjectMapper.toDto(subjectRepository.save(subjectMapper.toModel(subjectRequest)))
        if (subjectRequest.isParent == true) {
            subjectRequest.childs.forEach {
                it.uuidSchool = school
                it.isParent = false
                it.uuidParent = saved.uuid
            }
            saveMultiple(subjectRequest.childs)
        }
        return saved
    }

    @Transactional
    override fun saveMultiple(subjectRequestList: List<SubjectRequest>): List<SubjectDto> {
        log.trace("subject saveMultiple -> requestList: ${objectMapper.writeValueAsString(subjectRequestList)}")
        val subjects = subjectRequestList.map(subjectMapper::toModel)
        return subjectRepository.saveAll(subjects).map(subjectMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, subjectRequest: SubjectRequest, includeDelete: Boolean): SubjectDto {
        log.trace("subject update -> uuid: $uuid, request: $subjectRequest")
        val subject = if (!includeDelete) {
            getById(uuid)
        } else {
            subjectRepository.getByUuid(uuid).get()
        }
        subjectMapper.update(subjectRequest, subject)
        return subjectMapper.toDto(subjectRepository.save(subject))
    }

    @Transactional
    override fun updateMultiple(subjectDtoList: List<SubjectDto>): List<SubjectDto> {
        log.trace("subject updateMultiple -> subjectDtoList: ${objectMapper.writeValueAsString(subjectDtoList)}")
        val subjects = subjectRepository.findAllById(subjectDtoList.mapNotNull { it.uuid })
        subjects.forEach { subject ->
            subjectMapper.update(subjectMapper.toRequest(subjectDtoList.first { it.uuid == subject.uuid }), subject)
        }
        return subjectRepository.saveAll(subjects).map(subjectMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("subject delete -> uuid: $uuid")
        val subject = getById(uuid)
        subject.deleted = true
        subject.deletedAt = LocalDateTime.now()
        subjectRepository.save(subject)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("subject deleteMultiple -> uuid: $uuidList")
        val subjects = subjectRepository.findAllById(uuidList)
        subjects.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        subjectRepository.saveAll(subjects)
    }
}
