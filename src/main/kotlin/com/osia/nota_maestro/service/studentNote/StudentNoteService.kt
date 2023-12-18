package com.osia.nota_maestro.service.studentNote

import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteRequest
import com.osia.nota_maestro.model.StudentNote
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface StudentNoteService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): StudentNote
    fun findByMultiple(uuidList: List<UUID>): List<StudentNoteDto>
    fun findAll(pageable: Pageable, school: UUID): Page<StudentNoteDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentNoteDto>
    // Create
    fun save(studentNoteRequest: StudentNoteRequest, replace: Boolean = false): StudentNoteDto
    fun saveMultiple(studentNoteRequestList: List<StudentNoteRequest>): List<StudentNoteDto>
    // Update
    fun update(uuid: UUID, studentNoteRequest: StudentNoteRequest, includeDelete: Boolean = false): StudentNoteDto
    fun updateMultiple(studentNoteDtoList: List<StudentNoteDto>): List<StudentNoteDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
