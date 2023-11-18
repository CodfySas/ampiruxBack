package com.osia.nota_maestro.service.subModule.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.subModule.v1.SubModuleDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleMapper
import com.osia.nota_maestro.dto.subModule.v1.SubModuleRequest
import com.osia.nota_maestro.model.SubModule
import com.osia.nota_maestro.repository.subModule.SubModuleRepository
import com.osia.nota_maestro.service.subModule.SubModuleService
import com.osia.nota_maestro.util.CreateSpec
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

@Service("subModule.crud_service")
@Transactional
class SubModuleServiceImpl(
    private val subModuleRepository: SubModuleRepository,
    private val subModuleMapper: SubModuleMapper,
    private val objectMapper: ObjectMapper
) : SubModuleService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("subModule count -> increment: $increment")
        return subModuleRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): SubModule {
        return subModuleRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "SubModule $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<SubModuleDto> {
        log.trace("subModule findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return subModuleRepository.findAllById(uuidList).map(subModuleMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<SubModuleDto> {
        log.trace("subModule findAll -> pageable: $pageable")
        return subModuleRepository.findAll(Specification.where(CreateSpec<SubModule>().createSpec("")), pageable).map(subModuleMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<SubModuleDto> {
        log.trace("subModule findAllByFilter -> pageable: $pageable, where: $where")
        return subModuleRepository.findAll(Specification.where(CreateSpec<SubModule>().createSpec(where, school)), pageable).map(subModuleMapper::toDto)
    }

    @Transactional
    override fun save(subModuleRequest: SubModuleRequest): SubModuleDto {
        log.trace("subModule save -> request: $subModuleRequest")
        val subModule = subModuleMapper.toModel(subModuleRequest)
        return subModuleMapper.toDto(subModuleRepository.save(subModule))
    }

    @Transactional
    override fun saveMultiple(subModuleRequestList: List<SubModuleRequest>): List<SubModuleDto> {
        log.trace("subModule saveMultiple -> requestList: ${objectMapper.writeValueAsString(subModuleRequestList)}")
        val subModules = subModuleRequestList.map(subModuleMapper::toModel)
        return subModuleRepository.saveAll(subModules).map(subModuleMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, subModuleRequest: SubModuleRequest): SubModuleDto {
        log.trace("subModule update -> uuid: $uuid, request: $subModuleRequest")
        val subModule = getById(uuid)
        subModuleMapper.update(subModuleRequest, subModule)
        return subModuleMapper.toDto(subModuleRepository.save(subModule))
    }

    @Transactional
    override fun updateMultiple(subModuleDtoList: List<SubModuleDto>): List<SubModuleDto> {
        log.trace("subModule updateMultiple -> subModuleDtoList: ${objectMapper.writeValueAsString(subModuleDtoList)}")
        val subModules = subModuleRepository.findAllById(subModuleDtoList.mapNotNull { it.uuid })
        subModules.forEach { subModule ->
            subModuleMapper.update(subModuleMapper.toRequest(subModuleDtoList.first { it.uuid == subModule.uuid }), subModule)
        }
        return subModuleRepository.saveAll(subModules).map(subModuleMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("subModule delete -> uuid: $uuid")
        val subModule = getById(uuid)
        subModule.deleted = true
        subModule.deletedAt = LocalDateTime.now()
        subModuleRepository.save(subModule)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("subModule deleteMultiple -> uuid: $uuidList")
        val subModules = subModuleRepository.findAllById(uuidList)
        subModules.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        subModuleRepository.saveAll(subModules)
    }
}
