package com.osia.osia_erp.controller.auth.v1

import com.osia.osia_erp.dto.user.v1.UserDto
import com.osia.osia_erp.dto.user.v1.UserRequest
import com.osia.osia_erp.service.auth.AuthUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("auth.v1.crud")
@CrossOrigin
@RequestMapping("/auth")
@Validated
class AuthController(
    private val authService: AuthUseCase
) {
    @PostMapping("/login")
    fun findAll(@RequestBody userRequest: UserRequest): ResponseEntity<UserDto> {
        return ResponseEntity(authService.login(userRequest), HttpStatus.OK)
    }
}
