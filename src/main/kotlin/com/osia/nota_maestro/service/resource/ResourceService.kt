package com.osia.nota_maestro.service.resource

import com.osia.nota_maestro.dto.resource.v1.ResourceDto
import com.osia.nota_maestro.dto.resource.v1.ResourceRequest
import com.osia.nota_maestro.model.Resource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ResourceService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Resource
    fun findByMultiple(uuidList: List<UUID>): List<ResourceDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ResourceDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ResourceDto>
    // Create
    fun save(resourceRequest: ResourceRequest, school: UUID, replace: Boolean = false): ResourceDto
    fun saveMultiple(resourceRequestList: List<ResourceRequest>): List<ResourceDto>
    // Update
    fun update(uuid: UUID, resourceRequest: ResourceRequest, includeDelete: Boolean = false): ResourceDto
    fun updateMultiple(resourceDtoList: List<ResourceDto>): List<ResourceDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
