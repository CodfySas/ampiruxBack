package com.osia.logistic.need.service.user

import com.osia.logistic.need.dto.user.v1.UserDto
import com.osia.logistic.need.dto.user.v1.UserRequest
import com.osia.logistic.need.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

interface UserService {

    fun findByUuid(uuid: UUID): UserDto

    fun findAll(pageable: Pageable, specs: Specification<User>?): Page<UserDto>

    fun save(userRequest: UserRequest): UserDto

    fun update(uuid: UUID, userRequest: UserRequest): UserDto

    fun delete(uuid: UUID): UserDto

    fun getOne(uuid: UUID): User

    fun findByUuidIn(userList: List<UUID>): List<UserDto>
}
