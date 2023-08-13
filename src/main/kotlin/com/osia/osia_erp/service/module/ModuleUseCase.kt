package com.osia.osia_erp.service.module

import com.osia.osia_erp.dto.module.v1.ModuleDto
import com.osia.osia_erp.dto.module.v1.ModuleRequest
import com.osia.osia_erp.dto.module.v1.ModulesBuilderDto
import com.osia.osia_erp.dto.subModule.v1.SubModuleDto
import com.osia.osia_erp.dto.subModule.v1.SubModuleRequest
import com.osia.osia_erp.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.osia_erp.dto.subModuleUser.v1.SubModuleUserRequest
import java.util.UUID

interface ModuleUseCase {
    fun getModulesFromUserUuid(userUUID: UUID): List<ModulesBuilderDto>
    fun saveModule(moduleRequest: ModuleRequest): ModuleDto
    fun saveSubModule(subModuleRequest: SubModuleRequest): SubModuleDto
    fun saveSubModuleUser(subModuleUserRequest: SubModuleUserRequest): List<SubModuleUserDto>
}
