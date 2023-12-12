package com.osia.nota_maestro.service.gradeSubject

import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectDto
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectRequest
import com.osia.nota_maestro.model.GradeSubject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface GradeSubjectService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): GradeSubject
    fun findByMultiple(uuidList: List<UUID>): List<GradeSubjectDto>
    fun findAll(pageable: Pageable, school: UUID): Page<GradeSubjectDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<GradeSubjectDto>
    // Create
    fun save(gradeSubjectRequest: GradeSubjectRequest, replace: Boolean = false): GradeSubjectDto
    fun saveMultiple(gradeSubjectRequestList: List<GradeSubjectRequest>): List<GradeSubjectDto>
    // Update
    fun update(uuid: UUID, gradeSubjectRequest: GradeSubjectRequest, includeDelete: Boolean = false): GradeSubjectDto
    fun updateMultiple(gradeSubjectDtoList: List<GradeSubjectDto>): List<GradeSubjectDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
