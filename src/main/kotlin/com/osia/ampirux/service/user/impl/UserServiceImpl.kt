package com.osia.ampirux.service.user.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.dto.user.v1.UserRequest
import com.osia.ampirux.model.User
import com.osia.ampirux.repository.user.UserRepository
import com.osia.ampirux.service.user.UserService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val mapper: BaseMapper<UserRequest, User, UserDto>,
    private val objectMapper: ObjectMapper,
) : UserService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): User {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<UserDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<UserDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<User>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<UserDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<User>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: UserRequest, replace: Boolean): UserDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun saveMultiple(requestList: List<UserRequest>): List<UserDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: UserRequest, includeDelete: Boolean): UserDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun updateMultiple(dtoList: List<UserDto>): List<UserDto> {
        log.trace("updateMultiple -> dtoList: ${objectMapper.writeValueAsString(dtoList)}")
        val ids = dtoList.mapNotNull { it.uuid }
        val entities = repository.findAllById(ids)
        entities.forEach { entity ->
            val dto = dtoList.first { it.uuid == entity.uuid }
            mapper.update(mapper.toRequest(dto), entity)
        }
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun delete(id: UUID) {
        log.trace("delete -> id: $id")
        val entity = getById(id)
        entity.deleted = true
        entity.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        repository.save(entity)
    }

    @Transactional
    override fun deleteMultiple(idList: List<UUID>) {
        log.trace("deleteMultiple -> idList: $idList")
        val entities = repository.findAllById(idList)
        entities.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        }
        repository.saveAll(entities)
    }

    override fun getProfile(username: String, user: UUID): UserDto {
        val userFound = repository.getFistByUsernameIgnoreCase(username)
        return if (userFound.isPresent) {
            val userF = userFound.get()
            val userDto = mapper.toDto(userF)
            userDto
        } else {
            UserDto()
        }
    }

    override fun getByUsername(username: String): UserDto {
        return mapper.toDto(
            repository.getFistByUsernameIgnoreCase(username).orElseThrow {
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            }
        )
    }
}
