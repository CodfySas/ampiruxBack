package com.osia.nota_maestro.service.resource.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.resource.v1.ResourceDto
import com.osia.nota_maestro.dto.resource.v1.ResourceMapper
import com.osia.nota_maestro.dto.resource.v1.ResourceRequest
import com.osia.nota_maestro.dto.resources.v1.ResourceClassroomDto
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.dto.resources.v1.ResourceSubjectDto
import com.osia.nota_maestro.model.Resource
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.resource.ResourceRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.resource.ResourceService
import com.osia.nota_maestro.service.school.SchoolService
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

@Service("resource.crud_service")
@Transactional
class ResourceServiceImpl(
    private val resourceRepository: ResourceRepository,
    private val resourceMapper: ResourceMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomRepository: ClassroomRepository,
    private val gradeRepository: GradeRepository,
    private val subjectRepository: SubjectRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolService: SchoolService,
) : ResourceService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("resource count -> increment: $increment")
        return resourceRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Resource {
        return resourceRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Resource $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ResourceDto> {
        log.trace("resource findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return resourceRepository.findAllById(uuidList).map(resourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ResourceDto> {
        log.trace("resource findAll -> pageable: $pageable")
        return resourceRepository.findAll(Specification.where(CreateSpec<Resource>().createSpec("", school)), pageable).map(resourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ResourceDto> {
        log.trace("resource findAllByFilter -> pageable: $pageable, where: $where")
        return resourceRepository.findAll(Specification.where(CreateSpec<Resource>().createSpec(where, school)), pageable).map(resourceMapper::toDto)
    }

    @Transactional
    override fun save(resourceRequest: ResourceRequest, school: UUID, replace: Boolean): ResourceDto {
        log.trace("resource save -> request: $resourceRequest")
        resourceRequest.uuidSchool = school
        return resourceMapper.toDto(resourceRepository.save(resourceMapper.toModel(resourceRequest)))
    }

    @Transactional
    override fun saveMultiple(resourceRequestList: List<ResourceRequest>): List<ResourceDto> {
        log.trace("resource saveMultiple -> requestList: ${objectMapper.writeValueAsString(resourceRequestList)}")
        val resources = resourceRequestList.map(resourceMapper::toModel)
        return resourceRepository.saveAll(resources).map(resourceMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, resourceRequest: ResourceRequest, includeDelete: Boolean): ResourceDto {
        log.trace("resource update -> uuid: $uuid, request: $resourceRequest")
        val resource = if (!includeDelete) {
            getById(uuid)
        } else {
            resourceRepository.getByUuid(uuid).get()
        }
        resourceMapper.update(resourceRequest, resource)
        return resourceMapper.toDto(resourceRepository.save(resource))
    }

    @Transactional
    override fun updateMultiple(resourceDtoList: List<ResourceDto>): List<ResourceDto> {
        log.trace("resource updateMultiple -> resourceDtoList: ${objectMapper.writeValueAsString(resourceDtoList)}")
        val resources = resourceRepository.findAllById(resourceDtoList.mapNotNull { it.uuid })
        resources.forEach { resource ->
            resourceMapper.update(resourceMapper.toRequest(resourceDtoList.first { it.uuid == resource.uuid }), resource)
        }
        return resourceRepository.saveAll(resources).map(resourceMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("resource delete -> uuid: $uuid")
        val resource = getById(uuid)
        resource.deleted = true
        resource.deletedAt = LocalDateTime.now()
        resourceRepository.save(resource)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("resource deleteMultiple -> uuid: $uuidList")
        val resources = resourceRepository.findAllById(uuidList)
        resources.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        resourceRepository.saveAll(resources)
    }

    override fun my(uuid: UUID): List<ResourceGradeDto> {
        val userFound = userRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(userFound.uuidSchool!!)
        val grades = gradeRepository.findAllByUuidSchool(schoolFound.uuid!!).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeIn(grades.mapNotNull { it.uuid }.distinct())
        val classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid }.distinct())
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())

        return grades.map { g ->
            ResourceGradeDto().apply {
                this.uuid = g.uuid
                this.name = g.name
                this.classrooms = classrooms.filter { c -> c.uuidGrade == g.uuid }.map { c ->
                    ResourceClassroomDto().apply {
                        this.uuid = c.uuid
                        this.name = c.name
                        this.subjects =
                            classroomSubjects.filter { cs -> cs.uuidClassroom == c.uuid }.map { cs ->
                                ResourceSubjectDto().apply {
                                    val subject = subjects.firstOrNull { s -> s.uuid == cs.uuidSubject }
                                    this.uuid = subject?.uuid
                                    this.name = subject?.name
                                }
                            }
                    }
                }
            }
        }
    }
}
