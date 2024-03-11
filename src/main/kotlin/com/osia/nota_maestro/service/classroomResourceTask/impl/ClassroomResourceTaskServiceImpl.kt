package com.osia.nota_maestro.service.classroomResourceTask.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskDto
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskMapper
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskRequest
import com.osia.nota_maestro.model.ClassroomResourceTask
import com.osia.nota_maestro.repository.classroomResourceTask.ClassroomResourceTaskRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroomResource.ClassroomResourceService
import com.osia.nota_maestro.service.classroomResourceTask.ClassroomResourceTaskService
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

@Service("classroomResourceTask.crud_service")
@Transactional
class ClassroomResourceTaskServiceImpl(
    private val classroomResourceTaskRepository: ClassroomResourceTaskRepository,
    private val classroomResourceTaskMapper: ClassroomResourceTaskMapper,
    private val objectMapper: ObjectMapper,
    private val classroomResourceService: ClassroomResourceService,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val userRepository: UserRepository
) : ClassroomResourceTaskService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroomResourceTask count -> increment: $increment")
        return classroomResourceTaskRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ClassroomResourceTask {
        return classroomResourceTaskRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ClassroomResourceTask $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomResourceTaskDto> {
        log.trace("classroomResourceTask findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomResourceTaskRepository.findAllById(uuidList).map(classroomResourceTaskMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomResourceTaskDto> {
        log.trace("classroomResourceTask findAll -> pageable: $pageable")
        return classroomResourceTaskRepository.findAll(Specification.where(CreateSpec<ClassroomResourceTask>().createSpec("", school)), pageable).map(classroomResourceTaskMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomResourceTaskDto> {
        log.trace("classroomResourceTask findAllByFilter -> pageable: $pageable, where: $where")
        return classroomResourceTaskRepository.findAll(Specification.where(CreateSpec<ClassroomResourceTask>().createSpec(where, school)), pageable).map(classroomResourceTaskMapper::toDto)
    }

    @Transactional
    override fun save(classroomResourceTaskRequest: ClassroomResourceTaskRequest, replace: Boolean): ClassroomResourceTaskDto {
        log.trace("classroomResourceTask save -> request: $classroomResourceTaskRequest")
        val savedClassroomResourceTask = classroomResourceTaskMapper.toModel(classroomResourceTaskRequest)
        return classroomResourceTaskMapper.toDto(classroomResourceTaskRepository.save(savedClassroomResourceTask))
    }

    @Transactional
    override fun saveMultiple(classroomResourceTaskRequestList: List<ClassroomResourceTaskRequest>): List<ClassroomResourceTaskDto> {
        log.trace("classroomResourceTask saveMultiple -> requestList: ${objectMapper.writeValueAsString(classroomResourceTaskRequestList)}")
        val classroomResourceTasks = classroomResourceTaskRequestList.map(classroomResourceTaskMapper::toModel)
        return classroomResourceTaskRepository.saveAll(classroomResourceTasks).map(classroomResourceTaskMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, classroomResourceTaskRequest: ClassroomResourceTaskRequest, includeDelete: Boolean): ClassroomResourceTaskDto {
        log.trace("classroomResourceTask update -> uuid: $uuid, request: $classroomResourceTaskRequest")
        val classroomResourceTask = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomResourceTaskRepository.getByUuid(uuid).get()
        }
        classroomResourceTaskMapper.update(classroomResourceTaskRequest, classroomResourceTask)
        return classroomResourceTaskMapper.toDto(classroomResourceTaskRepository.save(classroomResourceTask))
    }

    @Transactional
    override fun updateMultiple(classroomResourceTaskDtoList: List<ClassroomResourceTaskDto>): List<ClassroomResourceTaskDto> {
        log.trace("classroomResourceTask updateMultiple -> classroomResourceTaskDtoList: ${objectMapper.writeValueAsString(classroomResourceTaskDtoList)}")
        val classroomResourceTasks = classroomResourceTaskRepository.findAllById(classroomResourceTaskDtoList.mapNotNull { it.uuid })
        classroomResourceTasks.forEach { classroomResourceTask ->
            classroomResourceTaskMapper.update(classroomResourceTaskMapper.toRequest(classroomResourceTaskDtoList.first { it.uuid == classroomResourceTask.uuid }), classroomResourceTask)
        }
        return classroomResourceTaskRepository.saveAll(classroomResourceTasks).map(classroomResourceTaskMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroomResourceTask delete -> uuid: $uuid")
        val classroomResourceTask = getById(uuid)
        classroomResourceTask.deleted = true
        classroomResourceTask.deletedAt = LocalDateTime.now()
        classroomResourceTaskRepository.save(classroomResourceTask)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroomResourceTask deleteMultiple -> uuid: $uuidList")
        val classroomResourceTasks = classroomResourceTaskRepository.findAllById(uuidList)
        classroomResourceTasks.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomResourceTaskRepository.saveAll(classroomResourceTasks)
    }

    override fun getByClassroomAndTask(classroom: UUID, task: UUID): List<ClassroomResourceTaskDto> {
        val classroomResourceTasks = classroomResourceTaskRepository.findAllByUuidClassroomResource(task)

        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(classroom)
        val users = userRepository.findAllById(classroomStudents.mapNotNull { it.uuidStudent }).sortedBy { it.name }.sortedBy { it.lastname }
        val sortedClassroomStudents = classroomStudents.sortedByDescending { classroomStudent ->
            val user = users.find { it.uuid == classroomStudent.uuidStudent }
            user?.lastname
        }
        return sortedClassroomStudents.map { cs-> ClassroomResourceTaskDto().apply {
            val my = users.firstOrNull { it.uuid == cs.uuidStudent }
            this.sname = my?.name
            this.slastname = my?.lastname

            val myTask = classroomResourceTasks.firstOrNull{ it.uuidClassroomStudent == cs.uuid }
            this.uuidClassroomResource = myTask?.uuidClassroomResource
            this.uuidStudent = cs.uuidStudent
            this.uuidClassroomStudent = cs.uuid
            this.name = myTask?.name ?: ""
            this.ext = myTask?.ext ?: ""
            this.hasFile = myTask?.hasFile ?: false
            this.description = myTask?.description ?: ""
            this.submitAt = myTask?.submitAt
            this.submitAtHour = myTask?.submitAtHour ?: ""
            this.note = myTask?.note
            this.observation = myTask?.observation ?: ""
        } }
    }

    override fun submitClassroomAndTask(
        classroom: UUID,
        task: UUID,
        req: List<ClassroomResourceTaskDto>
    ): List<ClassroomResourceTaskDto> {
        req.forEach { r->
            if(r.noteText != null && r.noteText != ""){
                r.note = r.noteText!!.replace(",",".").toDoubleOrNull()
            }
        }
        val toCreate = req.filter { it.uuid == null }.map {
            ClassroomResourceTaskRequest().apply {
                this.uuidClassroomResource = task
                this.uuidClassroomStudent = it.uuidClassroomStudent
                this.uuidStudent = it.uuidStudent
                this.note = it.note
                this.observation = it.observation
            }
        }
        val toUpdate = req.filter { it.uuid != null }
        saveMultiple(toCreate)
        updateMultiple(toUpdate)
        return req
    }
}
