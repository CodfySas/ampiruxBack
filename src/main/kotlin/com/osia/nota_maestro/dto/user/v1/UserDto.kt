package com.osia.nota_maestro.dto.user.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.model.enums.UserType
import java.util.UUID

class UserDto : BaseDto() {
    var color1: String? = null
    var color2: String? = null
    var username: String? = null
    var name: String? = null
    var role: UserType? = null
    var token: String? = null
    var uuidSchool: UUID? = null
    var schoolName: String? = null
    var dni: String? = null
    var documentType: String? = null
    var lastname: String? = null
    var uuidRole: UUID? = null
}
