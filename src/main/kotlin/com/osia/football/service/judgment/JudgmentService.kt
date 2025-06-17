package com.osia.nota_maestro.service.judgment

import com.osia.nota_maestro.dto.judgment.v1.JudgmentDto
import com.osia.nota_maestro.dto.judgment.v1.JudgmentRequest
import com.osia.nota_maestro.model.Judgment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface JudgmentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Judgment
    fun findByMultiple(uuidList: List<UUID>): List<JudgmentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<JudgmentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<JudgmentDto>
    // Create
    fun save(judgmentRequest: JudgmentRequest, replace: Boolean = false): JudgmentDto
    fun saveMultiple(judgmentRequestList: List<JudgmentRequest>): List<JudgmentDto>
    // Update
    fun update(uuid: UUID, judgmentRequest: JudgmentRequest, includeDelete: Boolean = false): JudgmentDto
    fun updateMultiple(judgmentDtoList: List<JudgmentDto>): List<JudgmentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
