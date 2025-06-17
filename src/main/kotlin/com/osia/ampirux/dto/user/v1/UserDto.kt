package com.osia.template.dto.user.v1

import com.osia.template.dto.BaseDto

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
}
