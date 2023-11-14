package com.osia.nota_maestro.dto.user.v1

import com.osia.nota_maestro.model.enums.UserType
import java.util.UUID

class UserRequest {
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var lastname: String? = null
    var documentType: String? = null
    var dni: String? = null
    var role: UserType? = null
    var uuidSchool: UUID? = null
    var schoolName: String? = null

    var phone: String? = null
    var email: String? = null
    var address: String? = null
}
