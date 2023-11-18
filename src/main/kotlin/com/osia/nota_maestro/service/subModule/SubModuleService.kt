package com.osia.nota_maestro.service.subModule

import com.osia.nota_maestro.dto.subModule.v1.SubModuleDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleRequest
import com.osia.nota_maestro.model.SubModule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SubModuleService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): SubModule
    fun findByMultiple(uuidList: List<UUID>): List<SubModuleDto>
    fun findAll(pageable: Pageable): Page<SubModuleDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SubModuleDto>
    // Create
    fun save(subModuleRequest: SubModuleRequest): SubModuleDto
    fun saveMultiple(subModuleRequestList: List<SubModuleRequest>): List<SubModuleDto>
    // Update
    fun update(uuid: UUID, subModuleRequest: SubModuleRequest): SubModuleDto
    fun updateMultiple(subModuleDtoList: List<SubModuleDto>): List<SubModuleDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
