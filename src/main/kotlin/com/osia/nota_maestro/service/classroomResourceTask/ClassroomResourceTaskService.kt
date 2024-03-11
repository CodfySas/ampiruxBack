package com.osia.nota_maestro.service.classroomResourceTask

import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskDto
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskRequest
import com.osia.nota_maestro.model.ClassroomResourceTask
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ClassroomResourceTaskService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ClassroomResourceTask
    fun findByMultiple(uuidList: List<UUID>): List<ClassroomResourceTaskDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ClassroomResourceTaskDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomResourceTaskDto>
    // Create
    fun save(classroomResourceTaskRequest: ClassroomResourceTaskRequest, replace: Boolean = false): ClassroomResourceTaskDto
    fun saveMultiple(classroomResourceTaskRequestList: List<ClassroomResourceTaskRequest>): List<ClassroomResourceTaskDto>
    // Update
    fun update(uuid: UUID, classroomResourceTaskRequest: ClassroomResourceTaskRequest, includeDelete: Boolean = false): ClassroomResourceTaskDto
    fun updateMultiple(classroomResourceTaskDtoList: List<ClassroomResourceTaskDto>): List<ClassroomResourceTaskDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getByClassroomAndTask(classroom: UUID, task: UUID): List<ClassroomResourceTaskDto>
    fun submitClassroomAndTask(classroom: UUID, task: UUID, req: List<ClassroomResourceTaskDto>): List<ClassroomResourceTaskDto>
}
