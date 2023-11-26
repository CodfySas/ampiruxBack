package com.osia.nota_maestro.service.teacher

import com.osia.nota_maestro.dto.teacher.v1.TeacherDto
import com.osia.nota_maestro.dto.teacher.v1.TeacherRequest
import com.osia.nota_maestro.model.Teacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface TeacherService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Teacher
    fun findByMultiple(uuidList: List<UUID>): List<TeacherDto>
    fun findAll(pageable: Pageable, school: UUID): Page<TeacherDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<TeacherDto>
    // Create
    fun save(teacherRequest: TeacherRequest, replace: Boolean = false): TeacherDto
    fun saveMultiple(teacherRequestList: List<TeacherRequest>): List<TeacherDto>
    // Update
    fun update(uuid: UUID, teacherRequest: TeacherRequest, includeDelete: Boolean = false): TeacherDto
    fun updateMultiple(teacherDtoList: List<TeacherDto>): List<TeacherDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
