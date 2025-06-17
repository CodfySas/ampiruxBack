package com.osia.ampirux.service.common

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CommonService<T, DTO, REQ> {
    // Read
    fun count(increment: Int = 0): Long
    fun getById(id: UUID): T
    fun findByMultiple(idList: List<UUID>): List<DTO>
    fun findAll(pageable: Pageable): Page<DTO>
    fun findAllByFilter(pageable: Pageable, where: String): Page<DTO>

    // Create
    fun save(request: REQ, replace: Boolean = false): DTO
    fun saveMultiple(requestList: List<REQ>): List<DTO>

    // Update
    fun update(id: UUID, request: REQ, includeDelete: Boolean = false): DTO
    fun updateMultiple(dtoList: List<DTO>): List<DTO>

    // Delete
    fun delete(id: UUID)
    fun deleteMultiple(idList: List<UUID>)
}
