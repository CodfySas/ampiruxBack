package com.osia.osia_erp.service.jwt

import com.osia.osia_erp.dto.user.v1.UserDto

interface JwtGenerator {
    fun generateToken(user: UserDto): String
}
