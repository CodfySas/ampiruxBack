package com.osia.nota_maestro.service.directorStudent

import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentRequest
import com.osia.nota_maestro.model.DirectorStudent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface DirectorStudentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): DirectorStudent
    fun findByMultiple(uuidList: List<UUID>): List<DirectorStudentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<DirectorStudentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DirectorStudentDto>
    // Create
    fun save(directorStudentRequest: DirectorStudentRequest, replace: Boolean = false): DirectorStudentDto
    fun saveMultiple(directorStudentRequestList: List<DirectorStudentRequest>): List<DirectorStudentDto>
    // Update
    fun update(uuid: UUID, directorStudentRequest: DirectorStudentRequest, includeDelete: Boolean = false): DirectorStudentDto
    fun updateMultiple(directorStudentDtoList: List<DirectorStudentDto>): List<DirectorStudentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
