package com.osia.nota_maestro.service.examAttempt

import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptDto
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptRequest
import com.osia.nota_maestro.model.ExamAttempt
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ExamAttemptService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ExamAttempt
    fun findByMultiple(uuidList: List<UUID>): List<ExamAttemptDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ExamAttemptDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamAttemptDto>
    // Create
    fun save(examAttemptRequest: ExamAttemptRequest, replace: Boolean = false): ExamAttemptDto
    fun saveMultiple(examAttemptRequestList: List<ExamAttemptRequest>): List<ExamAttemptDto>
    // Update
    fun update(uuid: UUID, examAttemptRequest: ExamAttemptRequest, includeDelete: Boolean = false): ExamAttemptDto
    fun updateMultiple(examAttemptDtoList: List<ExamAttemptDto>): List<ExamAttemptDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
