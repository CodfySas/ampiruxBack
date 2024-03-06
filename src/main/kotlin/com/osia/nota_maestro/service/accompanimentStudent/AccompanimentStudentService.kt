package com.osia.nota_maestro.service.accompanimentStudent

import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentRequest
import com.osia.nota_maestro.model.AccompanimentStudent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AccompanimentStudentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): AccompanimentStudent
    fun findByMultiple(uuidList: List<UUID>): List<AccompanimentStudentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<AccompanimentStudentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AccompanimentStudentDto>
    // Create
    fun save(accompanimentStudentRequest: AccompanimentStudentRequest, replace: Boolean = false): AccompanimentStudentDto
    fun saveMultiple(accompanimentStudentRequestList: List<AccompanimentStudentRequest>): List<AccompanimentStudentDto>
    // Update
    fun update(uuid: UUID, accompanimentStudentRequest: AccompanimentStudentRequest, includeDelete: Boolean = false): AccompanimentStudentDto
    fun updateMultiple(accompanimentStudentDtoList: List<AccompanimentStudentDto>): List<AccompanimentStudentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
