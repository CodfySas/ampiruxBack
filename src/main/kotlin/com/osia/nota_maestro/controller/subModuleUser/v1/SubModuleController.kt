package com.osia.nota_maestro.controller.subModuleUser.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.subModule.v1.SubModuleDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleMapper
import com.osia.nota_maestro.dto.subModule.v1.SubModuleRequest
import com.osia.nota_maestro.service.subModule.SubModuleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("subModule.v1.crud")
@CrossOrigin
@RequestMapping("v1/sub-modules")
@Validated
class SubModuleController(
    private val subModuleService: SubModuleService,
    private val subModuleMapper: SubModuleMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<SubModuleDto> {
        return subModuleService.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<SubModuleDto> {
        return subModuleService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return subModuleService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<SubModuleDto> {
        return ResponseEntity.ok().body(subModuleMapper.toDto(subModuleService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<SubModuleDto>> {
        return ResponseEntity.ok().body(subModuleService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: SubModuleRequest
    ): ResponseEntity<SubModuleDto> {
        return ResponseEntity(subModuleService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody subModuleRequestList: List<SubModuleRequest>
    ): ResponseEntity<List<SubModuleDto>> {
        return ResponseEntity(subModuleService.saveMultiple(subModuleRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: SubModuleRequest
    ): ResponseEntity<SubModuleDto> {
        return ResponseEntity.ok().body(subModuleService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody subModuleDtoList: List<SubModuleDto>
    ): ResponseEntity<List<SubModuleDto>> {
        return ResponseEntity.ok().body(subModuleService.updateMultiple(subModuleDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        subModuleService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        subModuleService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
