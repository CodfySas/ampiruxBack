package com.osia.osia_erp.service.company

import com.osia.osia_erp.dto.company.v1.CompanyDto
import com.osia.osia_erp.dto.company.v1.CompanyRequest
import com.osia.osia_erp.model.Company
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CompanyService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Company
    fun findByMultiple(uuidList: List<UUID>): List<CompanyDto>
    fun findAll(pageable: Pageable): Page<CompanyDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<CompanyDto>
    // Create
    fun save(companyRequest: CompanyRequest): CompanyDto
    fun saveMultiple(companyRequestList: List<CompanyRequest>): List<CompanyDto>
    // Update
    fun update(uuid: UUID, companyRequest: CompanyRequest): CompanyDto
    fun updateMultiple(companyDtoList: List<CompanyDto>): List<CompanyDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
