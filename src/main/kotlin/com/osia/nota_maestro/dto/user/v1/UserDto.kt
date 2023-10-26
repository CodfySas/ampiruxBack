package com.osia.nota_maestro.dto.user.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class UserDto : BaseDto() {
    var username: String? = null
    var name: String? = null
    var role: String? = null
    var token: String? = null
    var uuidCompany: UUID? = null
    var companyName: String? = null
}
