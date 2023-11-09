package com.osia.nota_maestro.controller.subModuleUser.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserDto
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserMapper
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.service.subModuleUser.SubModuleUserService
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("subModuleUser.v1.crud")
@CrossOrigin
@RequestMapping("v1/sub-module-users")
@Validated
class SubModuleUserController(
    private val subModuleUserService: SubModuleUserService,
    private val subModuleUserMapper: SubModuleUserMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable): Page<SubModuleUserDto> {
        return subModuleUserService.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<SubModuleUserDto> {
        return subModuleUserService.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return subModuleUserService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<SubModuleUserDto> {
        return ResponseEntity.ok().body(subModuleUserMapper.toDto(subModuleUserService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<SubModuleUserDto>> {
        return ResponseEntity.ok().body(subModuleUserService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: SubModuleUserRequest
    ): ResponseEntity<SubModuleUserDto> {
        return ResponseEntity(subModuleUserService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody subModuleUserRequestList: List<SubModuleUserRequest>
    ): ResponseEntity<List<SubModuleUserDto>> {
        return ResponseEntity(subModuleUserService.saveMultiple(subModuleUserRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: SubModuleUserRequest
    ): ResponseEntity<SubModuleUserDto> {
        return ResponseEntity.ok().body(subModuleUserService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody subModuleUserDtoList: List<SubModuleUserDto>
    ): ResponseEntity<List<SubModuleUserDto>> {
        return ResponseEntity.ok().body(subModuleUserService.updateMultiple(subModuleUserDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        subModuleUserService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        subModuleUserService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
