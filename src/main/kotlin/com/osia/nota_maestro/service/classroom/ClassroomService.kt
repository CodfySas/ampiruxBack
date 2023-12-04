package com.osia.nota_maestro.service.classroom

import com.osia.nota_maestro.dto.classroom.v1.ClassroomDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomRequest
import com.osia.nota_maestro.model.Classroom
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ClassroomService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Classroom
    fun findByMultiple(uuidList: List<UUID>): List<ClassroomDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ClassroomDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomDto>
    // Create
    fun save(classroomRequest: ClassroomRequest, replace: Boolean = false): ClassroomDto
    fun saveMultiple(classroomRequestList: List<ClassroomRequest>): List<ClassroomDto>
    // Update
    fun update(uuid: UUID, classroomRequest: ClassroomRequest, includeDelete: Boolean = false): ClassroomDto
    fun updateMultiple(classroomDtoList: List<ClassroomDto>): List<ClassroomDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
