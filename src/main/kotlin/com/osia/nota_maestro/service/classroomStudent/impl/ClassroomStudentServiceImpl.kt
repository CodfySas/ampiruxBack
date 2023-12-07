package com.osia.nota_maestro.service.classroomStudent.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomMapper
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentDto
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentMapper
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentRequest
import com.osia.nota_maestro.model.ClassroomStudent
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.service.classroomStudent.ClassroomStudentService
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
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("classroomStudent.crud_service")
@Transactional
class ClassroomStudentServiceImpl(
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val classroomStudentMapper: ClassroomStudentMapper,
    private val classroomMapper: ClassroomMapper,
    private val classroomRepository: ClassroomRepository,
    private val objectMapper: ObjectMapper
) : ClassroomStudentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroomStudent count -> increment: $increment")
        return classroomStudentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ClassroomStudent {
        return classroomStudentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ClassroomStudent $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomStudentDto> {
        log.trace("classroomStudent findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomStudentRepository.findAllById(uuidList).map(classroomStudentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomStudentDto> {
        log.trace("classroomStudent findAll -> pageable: $pageable")
        return classroomStudentRepository.findAll(Specification.where(CreateSpec<ClassroomStudent>().createSpec("", school)), pageable).map(classroomStudentMapper::toDto)
    }

    private fun emptySpec(where: String): Specification<ClassroomStudent> {
        var finalSpec = Specification { root: Root<ClassroomStudent>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        finalSpec = finalSpec.and { root: Root<ClassroomStudent>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
            root.get<UUID>(where.split(":")[0]).`in`(UUID.fromString(where.split(":")[1]))
        }
        return finalSpec
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID?): Page<ClassroomStudentDto> {
        log.trace("classroomStudent findAllByFilter -> pageable: $pageable, where: $where")
        return classroomStudentRepository.findAll(Specification.where(emptySpec(where)), pageable).map(classroomStudentMapper::toDto)
    }

    @Transactional
    override fun save(classroomStudentRequest: ClassroomStudentRequest, replace: Boolean): ClassroomStudentDto {
        log.trace("classroomStudent save -> request: $classroomStudentRequest")
        val savedClassroomStudent = classroomStudentMapper.toModel(classroomStudentRequest)
        return classroomStudentMapper.toDto(classroomStudentRepository.save(savedClassroomStudent))
    }

    @Transactional
    override fun saveMultiple(classroomStudentRequestList: List<ClassroomStudentRequest>): List<ClassroomStudentDto> {
        log.trace("classroomStudent saveMultiple -> requestList: ${objectMapper.writeValueAsString(classroomStudentRequestList)}")
        val classroomStudents = classroomStudentRequestList.map(classroomStudentMapper::toModel)
        return classroomStudentRepository.saveAll(classroomStudents).map(classroomStudentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, classroomStudentRequest: ClassroomStudentRequest, includeDelete: Boolean): ClassroomStudentDto {
        log.trace("classroomStudent update -> uuid: $uuid, request: $classroomStudentRequest")
        val classroomStudent = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomStudentRepository.getByUuid(uuid).get()
        }
        classroomStudentMapper.update(classroomStudentRequest, classroomStudent)
        return classroomStudentMapper.toDto(classroomStudentRepository.save(classroomStudent))
    }

    @Transactional
    override fun updateMultiple(classroomStudentDtoList: List<ClassroomStudentDto>): List<ClassroomStudentDto> {
        log.trace("classroomStudent updateMultiple -> classroomStudentDtoList: ${objectMapper.writeValueAsString(classroomStudentDtoList)}")
        val classroomStudents = classroomStudentRepository.findAllById(classroomStudentDtoList.mapNotNull { it.uuid })
        classroomStudents.forEach { classroomStudent ->
            classroomStudentMapper.update(classroomStudentMapper.toRequest(classroomStudentDtoList.first { it.uuid == classroomStudent.uuid }), classroomStudent)
        }
        return classroomStudentRepository.saveAll(classroomStudents).map(classroomStudentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroomStudent delete -> uuid: $uuid")
        val classroomStudent = getById(uuid)
        classroomStudent.deleted = true
        classroomStudent.deletedAt = LocalDateTime.now()
        classroomStudentRepository.save(classroomStudent)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroomStudent deleteMultiple -> uuid: $uuidList")
        val classroomStudents = classroomStudentRepository.findAllById(uuidList)
        classroomStudents.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomStudentRepository.saveAll(classroomStudents)
    }
}
