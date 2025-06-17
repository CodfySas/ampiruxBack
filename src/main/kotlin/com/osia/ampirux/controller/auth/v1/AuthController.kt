package com.osia.template.controller.auth.v1

import com.osia.template.dto.user.v1.UserDto
import com.osia.template.dto.user.v1.UserRequest
import com.osia.template.service.auth.AuthUseCase
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

    @PostMapping("/create")
    fun signUp(@RequestBody userRequest: UserRequest): ResponseEntity<UserDto> {
        return ResponseEntity(authService.signUp(userRequest), HttpStatus.OK)
    }

    @GetMapping("/check-username/{username}")
    fun findByUsername(@PathVariable username: String): ResponseEntity<Boolean> {
        return ResponseEntity(authService.checkUsername(username), HttpStatus.OK)
    }

    @GetMapping("/check-email/{email}")
    fun findByEmail(@PathVariable email: String): ResponseEntity<Boolean> {
        return ResponseEntity(authService.checkEmail(email), HttpStatus.OK)
    }
}
