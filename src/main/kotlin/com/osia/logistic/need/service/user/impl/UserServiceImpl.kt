package com.osia.logistic.need.service.user.impl

import com.osia.logistic.need.dto.user.v1.UserDto
import com.osia.logistic.need.dto.user.v1.UserMapper
import com.osia.logistic.need.dto.user.v1.UserRequest
import com.osia.logistic.need.model.User
import com.osia.logistic.need.repository.user.UserRepository
import com.osia.logistic.need.service.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service("user.crud_service")
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : UserService {

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, specs: Specification<User>?): Page<UserDto> {
        return userRepository.findAll(Specification.where(specs), pageable).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: UUID): UserDto {
        return userMapper.toDto(getOne(uuid))
    }

    @Transactional
    override fun save(userRequest: UserRequest): UserDto {
        val user = userMapper.toModel(userRequest)
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun update(uuid: UUID, userRequest: UserRequest): UserDto {
        val user = getOne(uuid)
        userMapper.updateModel(userRequest, user)
        return userMapper.toDto(userRepository.save(user))
    }

    override fun delete(uuid: UUID): UserDto {
        val user = getOne(uuid)
        val userDto = userMapper.toDto(user)
        userRepository.delete(user)
        return userDto
    }

    override fun findByUuidIn(userList: List<UUID>): List<UserDto> {
        return userRepository.findByUuidIn(userList).map(userMapper::toDto)
    }

    override fun getOne(uuid: UUID): User {
        return userRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "user $uuid not found")
        }
    }
}
