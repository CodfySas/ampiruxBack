package com.osia.template.service.auth

import com.osia.template.dto.user.v1.UserDto
import com.osia.template.dto.user.v1.UserRequest

interface AuthUseCase {
    fun login(userRequest: UserRequest): UserDto
    fun signUp(userRequest: UserRequest): UserDto
    fun checkUsername(username: String): Boolean
    fun checkEmail(email: String): Boolean
}
