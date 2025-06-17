package com.osia.ampirux.service.user

import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.dto.user.v1.UserRequest
import com.osia.ampirux.model.User
import com.osia.ampirux.service.common.CommonService
import java.util.UUID

interface UserService : CommonService<User, UserDto, UserRequest> {
    fun getProfile(username: String, user: UUID): UserDto
    fun getByUsername(username: String): UserDto
}
