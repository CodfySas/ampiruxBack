package com.osia.nota_maestro.service.grade

import com.osia.nota_maestro.dto.grade.v1.CourseInfoDto
import com.osia.nota_maestro.dto.grade.v1.GradeDto
import com.osia.nota_maestro.dto.grade.v1.GradeRequest
import com.osia.nota_maestro.dto.grade.v1.GradeSubjectDto
import com.osia.nota_maestro.model.Grade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface GradeService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Grade
    fun findByMultiple(uuidList: List<UUID>): List<GradeDto>
    fun findAll(pageable: Pageable, school: UUID): Page<GradeDto>
    fun findCompleteInfo(school: UUID): CourseInfoDto
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<GradeDto>
    // Create
    fun save(gradeRequest: GradeRequest, replace: Boolean = false): GradeDto
    fun saveComplete(grades: CourseInfoDto, school: UUID): CourseInfoDto
    fun saveMultiple(gradeRequestList: List<GradeRequest>): List<GradeDto>
    // Update
    fun update(uuid: UUID, gradeRequest: GradeRequest, includeDelete: Boolean = false): GradeDto
    fun updateMultiple(gradeDtoList: List<GradeDto>): List<GradeDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getGradeWithSubjects(school: UUID): List<GradeSubjectDto>
}
