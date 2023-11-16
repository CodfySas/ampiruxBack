package com.osia.nota_maestro.service.student

import com.osia.nota_maestro.dto.student.v1.StudentDto
import com.osia.nota_maestro.dto.student.v1.StudentRequest
import com.osia.nota_maestro.model.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface StudentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Student
    fun findByMultiple(uuidList: List<UUID>): List<StudentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<StudentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentDto>
    // Create
    fun save(studentRequest: StudentRequest): StudentDto
    fun saveMultiple(studentRequestList: List<StudentRequest>): List<StudentDto>
    // Update
    fun update(uuid: UUID, studentRequest: StudentRequest): StudentDto
    fun updateMultiple(studentDtoList: List<StudentDto>): List<StudentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
