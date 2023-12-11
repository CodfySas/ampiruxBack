package com.osia.nota_maestro.service.subject

import com.osia.nota_maestro.dto.subject.v1.SubjectDto
import com.osia.nota_maestro.dto.subject.v1.SubjectRequest
import com.osia.nota_maestro.model.Subject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SubjectService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Subject
    fun findByMultiple(uuidList: List<UUID>): List<SubjectDto>
    fun findAll(pageable: Pageable, school: UUID): Page<SubjectDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SubjectDto>
    // Create
    fun save(subjectRequest: SubjectRequest, school: UUID, replace: Boolean = false): SubjectDto
    fun saveMultiple(subjectRequestList: List<SubjectRequest>): List<SubjectDto>
    // Update
    fun update(uuid: UUID, subjectRequest: SubjectRequest, includeDelete: Boolean = false): SubjectDto
    fun updateMultiple(subjectDtoList: List<SubjectDto>): List<SubjectDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
