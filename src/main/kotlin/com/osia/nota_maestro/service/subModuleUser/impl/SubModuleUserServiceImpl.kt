package com.osia.nota_maestro.service.subModuleUser.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserMapper
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.model.SubModuleUser
import com.osia.nota_maestro.repository.subModuleUser.SubModuleUserRepository
import com.osia.nota_maestro.service.subModuleUser.SubModuleUserService
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

@Service("subModuleUser.crud_service")
@Transactional
class SubModuleUserServiceImpl(
    private val subModuleUserRepository: SubModuleUserRepository,
    private val subModuleUserMapper: SubModuleUserMapper,
    private val objectMapper: ObjectMapper
) : SubModuleUserService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("subModuleUser count -> increment: $increment")
        return subModuleUserRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): SubModuleUser {
        return subModuleUserRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "SubModuleUser $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<SubModuleUserDto> {
        log.trace("subModuleUser findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return subModuleUserRepository.findAllById(uuidList).map(subModuleUserMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<SubModuleUserDto> {
        log.trace("subModuleUser findAll -> pageable: $pageable")
        return subModuleUserRepository.findAll(pageable).map(subModuleUserMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<SubModuleUserDto> {
        log.trace("subModuleUser findAllByFilter -> pageable: $pageable, where: $where")
        return subModuleUserRepository.findAll(Specification.where(createSpec(where)), pageable).map(subModuleUserMapper::toDto)
    }

    @Transactional
    override fun save(subModuleUserRequest: SubModuleUserRequest): SubModuleUserDto {
        log.trace("subModuleUser save -> request: $subModuleUserRequest")
        val subModuleUser = subModuleUserMapper.toModel(subModuleUserRequest)
        return subModuleUserMapper.toDto(subModuleUserRepository.save(subModuleUser))
    }

    @Transactional
    override fun saveMultiple(subModuleUserRequestList: List<SubModuleUserRequest>): List<SubModuleUserDto> {
        log.trace("subModuleUser saveMultiple -> requestList: ${objectMapper.writeValueAsString(subModuleUserRequestList)}")
        val subModuleUsers = subModuleUserRequestList.map(subModuleUserMapper::toModel)
        return subModuleUserRepository.saveAll(subModuleUsers).map(subModuleUserMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, subModuleUserRequest: SubModuleUserRequest): SubModuleUserDto {
        log.trace("subModuleUser update -> uuid: $uuid, request: $subModuleUserRequest")
        val subModuleUser = getById(uuid)
        subModuleUserMapper.update(subModuleUserRequest, subModuleUser)
        return subModuleUserMapper.toDto(subModuleUserRepository.save(subModuleUser))
    }

    @Transactional
    override fun updateMultiple(subModuleUserDtoList: List<SubModuleUserDto>): List<SubModuleUserDto> {
        log.trace("subModuleUser updateMultiple -> subModuleUserDtoList: ${objectMapper.writeValueAsString(subModuleUserDtoList)}")
        val subModuleUsers = subModuleUserRepository.findAllById(subModuleUserDtoList.mapNotNull { it.uuid })
        subModuleUsers.forEach { subModuleUser ->
            subModuleUserMapper.update(subModuleUserMapper.toRequest(subModuleUserDtoList.first { it.uuid == subModuleUser.uuid }), subModuleUser)
        }
        return subModuleUserRepository.saveAll(subModuleUsers).map(subModuleUserMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("subModuleUser delete -> uuid: $uuid")
        val subModuleUser = getById(uuid)
        subModuleUser.deleted = true
        subModuleUser.deletedAt = LocalDateTime.now()
        subModuleUserRepository.save(subModuleUser)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("subModuleUser deleteMultiple -> uuid: $uuidList")
        val subModuleUsers = subModuleUserRepository.findAllById(uuidList)
        subModuleUsers.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        subModuleUserRepository.saveAll(subModuleUsers)
    }

    fun createSpec(where: String): Specification<SubModuleUser> {
        var finalSpec = Specification { root: Root<SubModuleUser>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<SubModuleUser>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
