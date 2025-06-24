package com.osia.ampirux.dto.user.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class UserDto : BaseDto() {
    var username: String? = null
    var name: String? = null
    var lastname: String? = null
    var role: String? = null
    var active: Boolean? = true
    var phone: String? = null
    var email: String? = null
    var image: String? = null
    var token: String? = null
    var barbershopUuid: UUID? = null
    var barbershopCode: String? = null
}
