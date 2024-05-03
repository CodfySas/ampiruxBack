package com.osia.nota_maestro.service.examUserResponse

import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseDto
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseRequest
import com.osia.nota_maestro.model.ExamUserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ExamUserResponseService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ExamUserResponse
    fun findByMultiple(uuidList: List<UUID>): List<ExamUserResponseDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ExamUserResponseDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamUserResponseDto>
    // Create
    fun save(examUserResponseRequest: ExamUserResponseRequest, replace: Boolean = false): ExamUserResponseDto
    fun saveMultiple(examUserResponseRequestList: List<ExamUserResponseRequest>): List<ExamUserResponseDto>
    // Update
    fun update(uuid: UUID, examUserResponseRequest: ExamUserResponseRequest, includeDelete: Boolean = false): ExamUserResponseDto
    fun updateMultiple(examUserResponseDtoList: List<ExamUserResponseDto>): List<ExamUserResponseDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
