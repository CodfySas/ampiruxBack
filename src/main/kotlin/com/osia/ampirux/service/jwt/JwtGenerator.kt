package com.osia.template.service.jwt

import com.osia.template.dto.user.v1.UserDto

interface JwtGenerator {
    fun generateToken(user: UserDto): String
}
