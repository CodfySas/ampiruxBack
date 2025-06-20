package com.osia.ampirux.controller.user.v1

import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.dto.user.v1.UserMapper
import com.osia.ampirux.dto.user.v1.UserRequest
import com.osia.ampirux.service.user.UserService
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
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("user.v1.crud")
@CrossOrigin
@RequestMapping("v1/users")
@Validated
class UserController(
    private val service: UserService,
    private val mapper: UserMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page<UserDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String,
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID): Page<UserDto> {
        return service.findAllByFilter(pageable, where, barbershopUuid)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<UserDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody request: UserRequest): ResponseEntity<UserDto> {
        request.barbershopUuid = barbershopUuid;
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody requestList: List<UserRequest>): ResponseEntity<List<UserDto>> {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PutMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: UserRequest, @RequestHeader user: UUID): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.update(user, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List<UserDto>): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(service.updateMultiple(dtoList))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        service.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(@RequestBody uuidList: List<UUID>): ResponseEntity<HttpStatus> {
        service.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/profile/{username}")
    fun getProfile(@RequestHeader user: UUID, @PathVariable username: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.getProfile(username, user))
    }
}
