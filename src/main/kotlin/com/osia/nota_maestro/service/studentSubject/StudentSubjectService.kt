package com.osia.nota_maestro.service.studentSubject

import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.model.StudentSubject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface StudentSubjectService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): StudentSubject
    fun findByMultiple(uuidList: List<UUID>): List<StudentSubjectDto>
    fun findAll(pageable: Pageable, school: UUID): Page<StudentSubjectDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentSubjectDto>
    // Create
    fun save(studentSubjectRequest: StudentSubjectRequest, replace: Boolean = false): StudentSubjectDto
    fun saveMultiple(studentSubjectRequestList: List<StudentSubjectRequest>): List<StudentSubjectDto>
    // Update
    fun update(uuid: UUID, studentSubjectRequest: StudentSubjectRequest): StudentSubjectDto
    fun updateMultiple(studentSubjectDtoList: List<StudentSubjectDto>): List<StudentSubjectDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
