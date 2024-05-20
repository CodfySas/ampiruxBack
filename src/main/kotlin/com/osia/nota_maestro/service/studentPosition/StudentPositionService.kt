package com.osia.nota_maestro.service.studentPosition

import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionDto
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionRequest
import com.osia.nota_maestro.model.StudentPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface StudentPositionService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): StudentPosition
    fun findByMultiple(uuidList: List<UUID>): List<StudentPositionDto>
    fun findAll(pageable: Pageable, school: UUID): Page<StudentPositionDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentPositionDto>
    // Create
    fun save(studentPositionRequest: StudentPositionRequest, replace: Boolean = false): StudentPositionDto
    fun saveMultiple(studentPositionRequestList: List<StudentPositionRequest>): List<StudentPositionDto>
    // Update
    fun update(uuid: UUID, studentPositionRequest: StudentPositionRequest, includeDelete: Boolean = false): StudentPositionDto
    fun updateMultiple(studentPositionDtoList: List<StudentPositionDto>): List<StudentPositionDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
