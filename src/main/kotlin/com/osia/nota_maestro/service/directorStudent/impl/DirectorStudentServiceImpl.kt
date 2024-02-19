package com.osia.nota_maestro.service.directorStudent.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentMapper
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentRequest
import com.osia.nota_maestro.model.DirectorStudent
import com.osia.nota_maestro.repository.directorStudent.DirectorStudentRepository
import com.osia.nota_maestro.service.directorStudent.DirectorStudentService
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

@Service("directorStudent.crud_service")
@Transactional
class DirectorStudentServiceImpl(
    private val directorStudentRepository: DirectorStudentRepository,
    private val directorStudentMapper: DirectorStudentMapper,
    private val objectMapper: ObjectMapper
) : DirectorStudentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("directorStudent count -> increment: $increment")
        return directorStudentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): DirectorStudent {
        return directorStudentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "DirectorStudent $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<DirectorStudentDto> {
        log.trace("directorStudent findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return directorStudentRepository.findAllById(uuidList).map(directorStudentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<DirectorStudentDto> {
        log.trace("directorStudent findAll -> pageable: $pageable")
        return directorStudentRepository.findAll(Specification.where(CreateSpec<DirectorStudent>().createSpec("", school)), pageable).map(directorStudentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DirectorStudentDto> {
        log.trace("directorStudent findAllByFilter -> pageable: $pageable, where: $where")
        return directorStudentRepository.findAll(Specification.where(CreateSpec<DirectorStudent>().createSpec(where, school)), pageable).map(directorStudentMapper::toDto)
    }

    @Transactional
    override fun save(directorStudentRequest: DirectorStudentRequest, replace: Boolean): DirectorStudentDto {
        log.trace("directorStudent save -> request: $directorStudentRequest")
        val savedDirectorStudent = directorStudentMapper.toModel(directorStudentRequest)
        return directorStudentMapper.toDto(directorStudentRepository.save(savedDirectorStudent))
    }

    @Transactional
    override fun saveMultiple(directorStudentRequestList: List<DirectorStudentRequest>): List<DirectorStudentDto> {
        log.trace("directorStudent saveMultiple -> requestList: ${objectMapper.writeValueAsString(directorStudentRequestList)}")
        val directorStudents = directorStudentRequestList.map(directorStudentMapper::toModel)
        return directorStudentRepository.saveAll(directorStudents).map(directorStudentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, directorStudentRequest: DirectorStudentRequest, includeDelete: Boolean): DirectorStudentDto {
        log.trace("directorStudent update -> uuid: $uuid, request: $directorStudentRequest")
        val directorStudent = if (!includeDelete) {
            getById(uuid)
        } else {
            directorStudentRepository.getByUuid(uuid).get()
        }
        directorStudentMapper.update(directorStudentRequest, directorStudent)
        return directorStudentMapper.toDto(directorStudentRepository.save(directorStudent))
    }

    @Transactional
    override fun updateMultiple(directorStudentDtoList: List<DirectorStudentDto>): List<DirectorStudentDto> {
        log.trace("directorStudent updateMultiple -> directorStudentDtoList: ${objectMapper.writeValueAsString(directorStudentDtoList)}")
        val directorStudents = directorStudentRepository.findAllById(directorStudentDtoList.mapNotNull { it.uuid })
        directorStudents.forEach { directorStudent ->
            directorStudentMapper.update(directorStudentMapper.toRequest(directorStudentDtoList.first { it.uuid == directorStudent.uuid }), directorStudent)
        }
        return directorStudentRepository.saveAll(directorStudents).map(directorStudentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("directorStudent delete -> uuid: $uuid")
        val directorStudent = getById(uuid)
        directorStudent.deleted = true
        directorStudent.deletedAt = LocalDateTime.now()
        directorStudentRepository.save(directorStudent)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("directorStudent deleteMultiple -> uuid: $uuidList")
        val directorStudents = directorStudentRepository.findAllById(uuidList)
        directorStudents.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        directorStudentRepository.saveAll(directorStudents)
    }
}
