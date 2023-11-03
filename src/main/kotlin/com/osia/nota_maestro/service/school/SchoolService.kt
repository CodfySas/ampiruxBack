package com.osia.nota_maestro.service.school

import com.osia.nota_maestro.dto.school.v1.SchoolDto
import com.osia.nota_maestro.dto.school.v1.SchoolRequest
import com.osia.nota_maestro.model.School
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SchoolService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): School
    fun findByMultiple(uuidList: List<UUID>): List<SchoolDto>
    fun findAll(pageable: Pageable): Page<SchoolDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<SchoolDto>
    // Create
    fun save(schoolRequest: SchoolRequest): SchoolDto
    fun saveMultiple(schoolRequestList: List<SchoolRequest>): List<SchoolDto>
    // Update
    fun update(uuid: UUID, schoolRequest: SchoolRequest): SchoolDto
    fun updateMultiple(schoolDtoList: List<SchoolDto>): List<SchoolDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
