package com.osia.nota_maestro.service.schoolPeriod

import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodDto
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodRequest
import com.osia.nota_maestro.model.SchoolPeriod
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SchoolPeriodService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): SchoolPeriod
    fun findByMultiple(uuidList: List<UUID>): List<SchoolPeriodDto>
    fun findAll(pageable: Pageable, school: UUID): Page<SchoolPeriodDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SchoolPeriodDto>
    // Create
    fun save(schoolPeriodRequest: SchoolPeriodRequest, replace: Boolean = false): SchoolPeriodDto
    fun saveMultiple(schoolPeriodRequestList: List<SchoolPeriodRequest>): List<SchoolPeriodDto>
    // Update
    fun update(uuid: UUID, schoolPeriodRequest: SchoolPeriodRequest, includeDelete: Boolean = false): SchoolPeriodDto
    fun updateMultiple(schoolPeriodDtoList: List<SchoolPeriodDto>): List<SchoolPeriodDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
