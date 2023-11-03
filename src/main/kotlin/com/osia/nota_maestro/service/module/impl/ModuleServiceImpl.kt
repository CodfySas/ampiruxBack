package com.osia.nota_maestro.service.module.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.module.v1.ModuleDto
import com.osia.nota_maestro.dto.module.v1.ModuleMapper
import com.osia.nota_maestro.dto.module.v1.ModuleRequest
import com.osia.nota_maestro.dto.module.v1.ModulesBuilderDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleMapper
import com.osia.nota_maestro.dto.subModule.v1.SubModuleRequest
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserMapper
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.model.SubModuleUser
import com.osia.nota_maestro.repository.module.ModuleRepository
import com.osia.nota_maestro.repository.subModule.SubModuleRepository
import com.osia.nota_maestro.repository.subModuleUser.SubModuleUserRepository
import com.osia.nota_maestro.service.module.ModuleUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service("module.service")
@Transactional
class ModuleServiceImpl(
    private val objectMapper: ObjectMapper,
    private val moduleMapper: ModuleMapper,
    private val subModuleUserMapper: SubModuleUserMapper,
    private val subModulesRepository: SubModuleRepository,
    private val moduleRepository: ModuleRepository,
    private val subModuleUserRepository: SubModuleUserRepository,
    private val subModulesMapper: SubModuleMapper
) : ModuleUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getModulesFromUserUuid(userUUID: UUID): List<ModulesBuilderDto> {
        log.trace("modules getModulesFromUserUuid -> userUUID: ${objectMapper.writeValueAsString(userUUID)}")
        val subModulesByUser = subModuleUserRepository.findAllByUuidUser(userUUID)
        if (subModulesByUser.isEmpty()) {
            return emptyList()
        }
        val finalList = mutableListOf<ModulesBuilderDto>()
        val subModules = subModulesRepository.findAllByUuidInOrderByOrdered(subModulesByUser.mapNotNull { it.uuidSubModule }.distinct())
        val modules = moduleRepository.findAllByUuidInOrderByOrdered(subModules.mapNotNull { it.uuidModule }.distinct())

        modules.forEach {
            val subModulesFromModule = subModules.filter { sm -> sm.uuidModule == it.uuid }
            finalList.add(
                ModulesBuilderDto().apply {
                    this.name = it.name
                    this.ordered = it.ordered
                    this.subModules = subModulesFromModule.map(subModulesMapper::toDto)
                }
            )
        }

        return finalList
    }

    override fun saveModule(moduleRequest: ModuleRequest): ModuleDto {
        log.trace("module saveModule -> request: ${objectMapper.writeValueAsString(moduleRequest)}")
        val module = moduleMapper.toModel(moduleRequest)
        return moduleMapper.toDto(moduleRepository.save(module))
    }

    override fun saveSubModule(subModuleRequest: SubModuleRequest): SubModuleDto {
        log.trace("module saveSubModule -> request: ${objectMapper.writeValueAsString(subModuleRequest)}")
        val subModule = subModulesMapper.toModel(subModuleRequest)
        return subModulesMapper.toDto(subModulesRepository.save(subModule))
    }

    override fun saveSubModuleUser(subModuleUserRequest: SubModuleUserRequest): List<SubModuleUserDto> {
        log.trace("module saveSubModuleUser -> request: ${objectMapper.writeValueAsString(subModuleUserRequest)}")
        val subModelUsers = mutableListOf<SubModuleUser>()
        subModuleUserRequest.uuidSubModules.forEach {
            subModelUsers.add(
                SubModuleUser().apply {
                    this.uuidUser = subModuleUserRequest.uuidUser
                    this.uuidSubModule = it
                }
            )
        }
        return subModuleUserRepository.saveAll(subModelUsers).map(subModuleUserMapper::toDto)
    }
}
