package com.osia.osia_erp.dto.user.v1

import java.util.UUID

class UserRequest {
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var role: String? = null
    var uuidCompany: UUID? = null
}
