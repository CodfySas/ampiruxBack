package com.osia.ampirux.dto.user.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class UserMinDto : BaseDto() {
    var username: String? = null
    var name: String? = null
    var lastname: String? = null
    var image: String? = null
    var barbershopUuid: UUID? = null
}
