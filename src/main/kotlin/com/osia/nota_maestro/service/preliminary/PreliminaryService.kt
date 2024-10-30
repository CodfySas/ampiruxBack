package com.osia.nota_maestro.service.preliminary

import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllRequest
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryRequest
import com.osia.nota_maestro.model.Preliminary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import java.util.UUID

interface PreliminaryService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Preliminary
    fun findByMultiple(uuidList: List<UUID>): List<PreliminaryDto>
    fun findAll(pageable: Pageable, school: UUID): Page<PreliminaryDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PreliminaryDto>
    // Create
    fun save(preliminaryRequest: PreliminaryRequest, replace: Boolean = false): PreliminaryDto
    fun saveMultiple(preliminaryRequestList: List<PreliminaryRequest>): List<PreliminaryDto>
    // Update
    fun update(uuid: UUID, preliminaryRequest: PreliminaryRequest, includeDelete: Boolean = false): PreliminaryDto
    fun updateMultiple(preliminaryDtoList: List<PreliminaryDto>): List<PreliminaryDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getByClassroom(preliminaryReq: PreliminaryAllRequest, user: UUID): List<PreliminaryAllDto>
    fun submit(req: List<PreliminaryAllDto>, period: Int, classroom: UUID): List<PreliminaryAllDto>
    fun repair()
}
