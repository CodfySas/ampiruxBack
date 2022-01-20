package com.osia.logistic.need.controller.user.v1

import com.osia.logistic.need.dto.request.OnCreate
import com.osia.logistic.need.dto.user.v1.UserDto
import com.osia.logistic.need.dto.user.v1.UserRequest
import com.osia.logistic.need.model.User
import com.osia.logistic.need.service.user.UserService
import com.sipios.springsearch.anotation.SearchSpec
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("user.v1.crud")
@RequestMapping("v1/users")
@Api(tags = ["users", "crud"])
@Validated
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun index(pageable: Pageable, @SearchSpec specs: Specification<User>?): Page<UserDto> {
        return userService.findAll(pageable, specs)
    }

    @GetMapping("/{uuid}")
    fun show(@PathVariable uuid: UUID): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findByUuid(uuid))
    }

    @PostMapping
    fun create(
        @Validated(OnCreate::class) @RequestBody request: UserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.save(request))
    }

    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: UserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.update(uuid, request))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.delete(uuid))
    }

    @PostMapping("/multiple")
    fun findByMany(@RequestBody userList: List<UUID>): List<UserDto> {
        return userService.findByUuidIn(userList)
    }
}
