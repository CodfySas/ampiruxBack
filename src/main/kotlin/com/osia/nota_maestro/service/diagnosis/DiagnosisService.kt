package com.osia.nota_maestro.service.diagnosis

import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisCompleteDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisRequest
import com.osia.nota_maestro.model.Diagnosis
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface DiagnosisService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Diagnosis
    fun findByMultiple(uuidList: List<UUID>): List<DiagnosisDto>
    fun findAll(pageable: Pageable, school: UUID): Page<DiagnosisDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DiagnosisDto>
    // Create
    fun save(diagnosisRequest: DiagnosisRequest, replace: Boolean = false): DiagnosisDto
    fun saveMultiple(diagnosisRequestList: List<DiagnosisRequest>): List<DiagnosisDto>
    // Update
    fun update(uuid: UUID, diagnosisRequest: DiagnosisRequest, includeDelete: Boolean = false): DiagnosisDto
    fun updateMultiple(diagnosisDtoList: List<DiagnosisDto>): List<DiagnosisDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getComplete(school: UUID): DiagnosisCompleteDto
    fun submitComplete(req: List<DiagnosisDto>, school: UUID): DiagnosisCompleteDto
}
