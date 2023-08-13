package com.osia.osia_erp.controller.module.v1

import com.osia.osia_erp.dto.module.v1.ModuleDto
import com.osia.osia_erp.dto.module.v1.ModuleRequest
import com.osia.osia_erp.dto.module.v1.ModulesBuilderDto
import com.osia.osia_erp.dto.subModule.v1.SubModuleDto
import com.osia.osia_erp.dto.subModule.v1.SubModuleRequest
import com.osia.osia_erp.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.osia_erp.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.osia_erp.service.module.ModuleUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("module.v1.crud")
@CrossOrigin
@RequestMapping("/modules")
@Validated
class ModuleController(
    private val moduleService: ModuleUseCase,
) {
    @GetMapping("/get-modules/{uuidUser}")
    fun findModulesByUser(@PathVariable uuidUser: UUID): ResponseEntity<List<ModulesBuilderDto>> {
        return ResponseEntity(moduleService.getModulesFromUserUuid(uuidUser), HttpStatus.OK)
    }

    @PostMapping("/module")
    fun createModule(@RequestBody moduleRequest: ModuleRequest): ResponseEntity<ModuleDto> {
        return ResponseEntity(moduleService.saveModule(moduleRequest), HttpStatus.OK)
    }

    @PostMapping("/sub-module")
    fun createSubModule(@RequestBody subModuleRequest: SubModuleRequest): ResponseEntity<SubModuleDto> {
        return ResponseEntity(moduleService.saveSubModule(subModuleRequest), HttpStatus.OK)
    }

    @PostMapping("/sub-module-user")
    fun createSubModuleUser(@RequestBody subModuleUserRequest: SubModuleUserRequest): ResponseEntity<List<SubModuleUserDto>> {
        return ResponseEntity(moduleService.saveSubModuleUser(subModuleUserRequest), HttpStatus.OK)
    }
}
