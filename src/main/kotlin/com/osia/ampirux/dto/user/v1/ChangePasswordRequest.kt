package com.osia.ampirux.dto.user.v1

import java.util.UUID

class ChangePasswordRequest {
    var uuidUser: UUID? = null
    var actualPassword: String? = null
    var password: String? = null
}
