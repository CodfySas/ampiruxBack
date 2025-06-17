package com.osia.nota_maestro.service.auth

import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserRequest

interface AuthUseCase {
    fun login(userRequest: UserRequest): UserDto
}
