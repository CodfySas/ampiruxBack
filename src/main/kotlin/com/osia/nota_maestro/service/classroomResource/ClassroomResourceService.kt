package com.osia.nota_maestro.service.classroomResource

import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceDto
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceRequest
import com.osia.nota_maestro.dto.classroomResource.v1.ExamCompleteDto
import com.osia.nota_maestro.model.ClassroomResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import java.util.UUID

interface ClassroomResourceService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ClassroomResource
    fun findByMultiple(uuidList: List<UUID>): List<ClassroomResourceDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ClassroomResourceDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomResourceDto>
    // Create
    fun save(classroomResourceRequest: ClassroomResourceRequest, replace: Boolean = false): ClassroomResourceDto
    fun saveMultiple(classroomResourceRequestList: List<ClassroomResourceRequest>): List<ClassroomResourceDto>
    // Update
    fun update(uuid: UUID, classroomResourceRequest: ClassroomResourceRequest, includeDelete: Boolean = false): ClassroomResourceDto
    fun updateMultiple(classroomResourceDtoList: List<ClassroomResourceDto>): List<ClassroomResourceDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getBy(classroom: UUID, subject: UUID): List<List<ClassroomResourceDto>>

    fun getByMy(uuid: UUID, subject: UUID): List<List<ClassroomResourceDto>>


    fun download(uuid: UUID): ResponseEntity<ByteArray>

    fun getCompleteExamByTeacher(uuid: UUID, task: UUID): ExamCompleteDto

    fun submitExam(exam: ExamCompleteDto, classroom: UUID, subject: UUID, period: Int): ExamCompleteDto
}
