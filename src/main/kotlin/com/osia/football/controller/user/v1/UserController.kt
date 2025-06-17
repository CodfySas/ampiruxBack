package com.osia.nota_maestro.controller.user.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.user.v1.ChangePasswordRequest
import com.osia.nota_maestro.dto.user.v1.SavedMultipleUserDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.service.user.UserService
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

@RestController("user.v1.crud")
@CrossOrigin
@RequestMapping("v1/users")
@Validated
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<UserDto> {
        return userService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<UserDto> {
        return userService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return userService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<UserDto> {
        return ResponseEntity.ok().body(userMapper.toDto(userService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok().body(userService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: UserRequest,
        @RequestHeader school: UUID
    ): ResponseEntity<UserDto> {
        return ResponseEntity(userService.save(request, school), HttpStatus.CREATED)
    }

    @PostMapping("/multiple/admins")
    fun saveMultipleAdmins(
        @Validated(OnCreate::class) @RequestBody userRequestList: List<UserRequest>,
        @RequestHeader school: UUID
    ): ResponseEntity<SavedMultipleUserDto> {
        return ResponseEntity(userService.saveMultiple(userRequestList, school, "admin"), HttpStatus.CREATED)
    }

    @PostMapping("/multiple/students")
    fun saveMultipleStudents(
        @Validated(OnCreate::class) @RequestBody userRequestList: List<UserRequest>,
        @RequestHeader school: UUID
    ): ResponseEntity<SavedMultipleUserDto> {
        return ResponseEntity(userService.saveMultiple(userRequestList, school, "student"), HttpStatus.CREATED)
    }

    @PostMapping("/multiple/teachers")
    fun saveMultipleTeachers(
        @Validated(OnCreate::class) @RequestBody userRequestList: List<UserRequest>,
        @RequestHeader school: UUID
    ): ResponseEntity<SavedMultipleUserDto> {
        return ResponseEntity(userService.saveMultiple(userRequestList, school, "teacher"), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: UserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok().body(userService.update(uuid, request))
    }

    @PostMapping("update-password")
    fun changePass(
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok().body(userService.changePassword(request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody userDtoList: List<UserDto>
    ): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok().body(userService.updateMultiple(userDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        userService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        userService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
