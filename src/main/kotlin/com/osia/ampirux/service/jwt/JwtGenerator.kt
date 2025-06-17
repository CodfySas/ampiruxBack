package com.osia.ampirux.service.jwt

import com.osia.ampirux.dto.user.v1.UserDto

interface JwtGenerator {
    fun generateToken(user: UserDto): String
}
