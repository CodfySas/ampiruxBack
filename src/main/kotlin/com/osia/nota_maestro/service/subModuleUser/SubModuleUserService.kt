package com.osia.nota_maestro.service.subModuleUser

import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.model.SubModuleUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SubModuleUserService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): SubModuleUser
    fun findByMultiple(uuidList: List<UUID>): List<SubModuleUserDto>
    fun findAll(pageable: Pageable): Page<SubModuleUserDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<SubModuleUserDto>
    // Create
    fun save(subModuleUserRequest: SubModuleUserRequest): SubModuleUserDto
    fun saveMultiple(subModuleUserRequestList: List<SubModuleUserRequest>): List<SubModuleUserDto>
    // Update
    fun update(uuid: UUID, subModuleUserRequest: SubModuleUserRequest): SubModuleUserDto
    fun updateMultiple(subModuleUserDtoList: List<SubModuleUserDto>): List<SubModuleUserDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
