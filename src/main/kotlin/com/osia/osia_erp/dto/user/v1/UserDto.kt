package com.osia.osia_erp.dto.user.v1

import com.osia.osia_erp.dto.BaseDto
import java.util.UUID

class UserDto : BaseDto() {
    var username: String? = null
    var name: String? = null
    var role: String? = null
    var token: String? = null
    var uuidCompany: UUID? = null
    var companyName: String? = null
}
