package com.osia.nota_maestro.service.jwt

import com.osia.nota_maestro.dto.user.v1.UserDto

interface JwtGenerator {
    fun generateToken(user: UserDto): String
}
