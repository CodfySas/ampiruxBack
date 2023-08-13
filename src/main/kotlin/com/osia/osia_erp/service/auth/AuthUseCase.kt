package com.osia.osia_erp.service.auth

import com.osia.osia_erp.dto.user.v1.UserDto
import com.osia.osia_erp.dto.user.v1.UserRequest

interface AuthUseCase {
    fun login(userRequest: UserRequest): UserDto
}
