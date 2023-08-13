package com.osia.osia_erp.service.user.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.osia_erp.dto.user.v1.UserDto
import com.osia.osia_erp.dto.user.v1.UserMapper
import com.osia.osia_erp.dto.user.v1.UserRequest
import com.osia.osia_erp.model.User
import com.osia.osia_erp.repository.user.UserRepository
import com.osia.osia_erp.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("user.crud_service")
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val objectMapper: ObjectMapper
) : UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("user count -> increment: $increment")
        return userRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): User {
        return userRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<UserDto> {
        log.trace("user findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return userRepository.findAllById(uuidList).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<UserDto> {
        log.trace("user findAll -> pageable: $pageable")
        return userRepository.findAll(pageable).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<UserDto> {
        log.trace("user findAllByFilter -> pageable: $pageable, where: $where")
        return userRepository.findAll(Specification.where(createSpec(where)), pageable).map(userMapper::toDto)
    }

    @Transactional
    override fun save(userRequest: UserRequest): UserDto {
        log.trace("user save -> request: $userRequest")
        val user = userMapper.toModel(userRequest)
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun saveMultiple(userRequestList: List<UserRequest>): List<UserDto> {
        log.trace("user saveMultiple -> requestList: ${objectMapper.writeValueAsString(userRequestList)}")
        val users = userRequestList.map(userMapper::toModel)
        return userRepository.saveAll(users).map(userMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, userRequest: UserRequest): UserDto {
        log.trace("user update -> uuid: $uuid, request: $userRequest")
        val user = getById(uuid)
        userMapper.update(userRequest, user)
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun updateMultiple(userDtoList: List<UserDto>): List<UserDto> {
        log.trace("user updateMultiple -> userDtoList: ${objectMapper.writeValueAsString(userDtoList)}")
        val users = userRepository.findAllById(userDtoList.mapNotNull { it.uuid })
        users.forEach { user ->
            userMapper.update(userMapper.toRequest(userDtoList.first { it.uuid == user.uuid }), user)
        }
        return userRepository.saveAll(users).map(userMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("user delete -> uuid: $uuid")
        val user = getById(uuid)
        user.deleted = true
        user.deletedAt = LocalDateTime.now()
        userRepository.save(user)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("user deleteMultiple -> uuid: $uuidList")
        val users = userRepository.findAllById(uuidList)
        users.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        userRepository.saveAll(users)
    }

    fun createSpec(where: String): Specification<User> {
        var finalSpec = Specification { root: Root<User>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<User>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
