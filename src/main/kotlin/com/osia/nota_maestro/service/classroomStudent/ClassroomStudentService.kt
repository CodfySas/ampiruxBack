package com.osia.nota_maestro.service.classroomStudent

import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentDto
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentRequest
import com.osia.nota_maestro.model.ClassroomStudent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ClassroomStudentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ClassroomStudent
    fun findByMultiple(uuidList: List<UUID>): List<ClassroomStudentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ClassroomStudentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID?): Page<ClassroomStudentDto>
    // Create
    fun save(classroomStudentRequest: ClassroomStudentRequest, replace: Boolean = false): ClassroomStudentDto
    fun saveMultiple(classroomStudentRequestList: List<ClassroomStudentRequest>): List<ClassroomStudentDto>
    // Update
    fun update(uuid: UUID, classroomStudentRequest: ClassroomStudentRequest, includeDelete: Boolean = false): ClassroomStudentDto
    fun updateMultiple(classroomStudentDtoList: List<ClassroomStudentDto>): List<ClassroomStudentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
