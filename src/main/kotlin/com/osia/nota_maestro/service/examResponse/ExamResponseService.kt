package com.osia.nota_maestro.service.examResponse

import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseRequest
import com.osia.nota_maestro.model.ExamResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ExamResponseService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ExamResponse
    fun findByMultiple(uuidList: List<UUID>): List<ExamResponseDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ExamResponseDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamResponseDto>
    // Create
    fun save(examResponseRequest: ExamResponseRequest, replace: Boolean = false): ExamResponseDto
    fun saveMultiple(examResponseRequestList: List<ExamResponseRequest>): List<ExamResponseDto>
    // Update
    fun update(uuid: UUID, examResponseRequest: ExamResponseRequest, includeDelete: Boolean = false): ExamResponseDto
    fun updateMultiple(examResponseDtoList: List<ExamResponseDto>): List<ExamResponseDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
