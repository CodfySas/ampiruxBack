package com.osia.ampirux.dto.user.v1

import java.util.UUID

class UserRequest {
    var username: String? = null
    var name: String? = null
    var lastname: String? = null
    var password: String? = null
    var role: String? = null
    var active: Boolean? = true
    var phone: String? = null
    var email: String? = null
    var image: String? = null
    var token: String? = null
    var barbershopUuid: UUID? = null
}
