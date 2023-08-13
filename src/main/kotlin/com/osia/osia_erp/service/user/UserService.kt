package com.osia.osia_erp.service.user

import com.osia.osia_erp.dto.user.v1.UserDto
import com.osia.osia_erp.dto.user.v1.UserRequest
import com.osia.osia_erp.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface UserService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): User
    fun findByMultiple(uuidList: List<UUID>): List<UserDto>
    fun findAll(pageable: Pageable): Page<UserDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<UserDto>
    // Create
    fun save(userRequest: UserRequest): UserDto
    fun saveMultiple(userRequestList: List<UserRequest>): List<UserDto>
    // Update
    fun update(uuid: UUID, userRequest: UserRequest): UserDto
    fun updateMultiple(userDtoList: List<UserDto>): List<UserDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
