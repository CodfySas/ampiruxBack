package com.osia.nota_maestro.service.classroomSubject

import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectCompleteDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectRequest
import com.osia.nota_maestro.dto.classroomSubject.v1.CompleteSubjectsTeachersDto
import com.osia.nota_maestro.model.ClassroomSubject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ClassroomSubjectService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ClassroomSubject
    fun findByMultiple(uuidList: List<UUID>): List<ClassroomSubjectDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ClassroomSubjectDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomSubjectDto>
    // Create
    fun save(classroomSubjectRequest: ClassroomSubjectRequest, replace: Boolean = false): ClassroomSubjectDto
    fun saveMultiple(classroomSubjectRequestList: List<ClassroomSubjectRequest>): List<ClassroomSubjectDto>
    // Update
    fun update(uuid: UUID, classroomSubjectRequest: ClassroomSubjectRequest, includeDelete: Boolean = false): ClassroomSubjectDto
    fun updateMultiple(classroomSubjectDtoList: List<ClassroomSubjectDto>): List<ClassroomSubjectDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getCompleteInfo(school: UUID): List<ClassroomSubjectCompleteDto>

    fun getCompleteInfo2(school: UUID): CompleteSubjectsTeachersDto
    fun saveCompleteInfo(toSave: CompleteSubjectsTeachersDto, school: UUID): CompleteSubjectsTeachersDto
}
