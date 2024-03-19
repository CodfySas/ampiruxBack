package com.osia.nota_maestro.service.examQuestion

import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionRequest
import com.osia.nota_maestro.model.ExamQuestion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ExamQuestionService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): ExamQuestion
    fun findByMultiple(uuidList: List<UUID>): List<ExamQuestionDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ExamQuestionDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ExamQuestionDto>
    // Create
    fun save(examQuestionRequest: ExamQuestionRequest, replace: Boolean = false): ExamQuestionDto
    fun saveMultiple(examQuestionRequestList: List<ExamQuestionRequest>): List<ExamQuestionDto>
    // Update
    fun update(uuid: UUID, examQuestionRequest: ExamQuestionRequest, includeDelete: Boolean = false): ExamQuestionDto
    fun updateMultiple(examQuestionDtoList: List<ExamQuestionDto>): List<ExamQuestionDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
