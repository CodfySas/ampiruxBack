package com.osia.nota_maestro.service.director

import com.osia.nota_maestro.dto.director.v1.DirectorCompleteDto
import com.osia.nota_maestro.dto.director.v1.DirectorDto
import com.osia.nota_maestro.dto.director.v1.DirectorRequest
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.model.Director
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface DirectorService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Director
    fun findByMultiple(uuidList: List<UUID>): List<DirectorDto>
    fun findAll(pageable: Pageable, school: UUID): Page<DirectorDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DirectorDto>
    // Create
    fun save(directorRequest: DirectorRequest, replace: Boolean = false): DirectorDto
    fun saveMultiple(directorRequestList: List<DirectorRequest>): List<DirectorDto>
    // Update
    fun update(uuid: UUID, directorRequest: DirectorRequest, includeDelete: Boolean = false): DirectorDto
    fun updateMultiple(directorDtoList: List<DirectorDto>): List<DirectorDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getComplete(school: UUID): List<DirectorCompleteDto>
    fun saveComplete(complete: List<DirectorCompleteDto>): List<DirectorCompleteDto>

    fun getMyGroups(uuid: UUID, school: UUID): List<DirectorCompleteDto>
    fun getByClassroom(uuid: UUID, period: Int): List<DirectorStudentDto>
}
