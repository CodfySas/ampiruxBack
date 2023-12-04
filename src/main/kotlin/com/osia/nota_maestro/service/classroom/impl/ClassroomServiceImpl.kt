package com.osia.nota_maestro.service.classroom.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomRequest
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.service.classroom.ClassroomService
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

@Service("classroom.crud_service")
@Transactional
class ClassroomServiceImpl(
    private val classroomRepository: ClassroomRepository,
    private val classroomMapper: ClassroomMapper,
    private val objectMapper: ObjectMapper
) : ClassroomService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroom count -> increment: $increment")
        return classroomRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Classroom {
        return classroomRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Classroom $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomDto> {
        log.trace("classroom findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomRepository.findAllById(uuidList).map(classroomMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomDto> {
        log.trace("classroom findAll -> pageable: $pageable")
        return classroomRepository.findAll(Specification.where(CreateSpec<Classroom>().createSpec("", school)), pageable).map(classroomMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomDto> {
        log.trace("classroom findAllByFilter -> pageable: $pageable, where: $where")
        return classroomRepository.findAll(Specification.where(CreateSpec<Classroom>().createSpec(where, school)), pageable).map(classroomMapper::toDto)
    }

    @Transactional
    override fun save(classroomRequest: ClassroomRequest, replace: Boolean): ClassroomDto {
        log.trace("classroom save -> request: $classroomRequest")
        val savedClassroom = classroomMapper.toModel(classroomRequest)
        return classroomMapper.toDto(classroomRepository.save(savedClassroom))
    }

    @Transactional
    override fun saveMultiple(classroomRequestList: List<ClassroomRequest>): List<ClassroomDto> {
        log.trace("classroom saveMultiple -> requestList: ${objectMapper.writeValueAsString(classroomRequestList)}")
        val classrooms = classroomRequestList.map(classroomMapper::toModel)
        return classroomRepository.saveAll(classrooms).map(classroomMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, classroomRequest: ClassroomRequest, includeDelete: Boolean): ClassroomDto {
        log.trace("classroom update -> uuid: $uuid, request: $classroomRequest")
        val classroom = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomRepository.getByUuid(uuid).get()
        }
        classroomMapper.update(classroomRequest, classroom)
        return classroomMapper.toDto(classroomRepository.save(classroom))
    }

    @Transactional
    override fun updateMultiple(classroomDtoList: List<ClassroomDto>): List<ClassroomDto> {
        log.trace("classroom updateMultiple -> classroomDtoList: ${objectMapper.writeValueAsString(classroomDtoList)}")
        val classrooms = classroomRepository.findAllById(classroomDtoList.mapNotNull { it.uuid })
        classrooms.forEach { classroom ->
            classroomMapper.update(classroomMapper.toRequest(classroomDtoList.first { it.uuid == classroom.uuid }), classroom)
        }
        return classroomRepository.saveAll(classrooms).map(classroomMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroom delete -> uuid: $uuid")
        val classroom = getById(uuid)
        classroom.deleted = true
        classroom.deletedAt = LocalDateTime.now()
        classroomRepository.save(classroom)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroom deleteMultiple -> uuid: $uuidList")
        val classrooms = classroomRepository.findAllById(uuidList)
        classrooms.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomRepository.saveAll(classrooms)
    }
}
