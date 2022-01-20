package com.osia.logistic.need.dto.post.v1

import com.osia.logistic.need.dto.BaseDto
import com.osia.logistic.need.dto.user.v1.UserDto

open class PostDto : BaseDto() {
    var description: String = ""
    var comments: Int = 0
    var userDto: UserDto = UserDto()
}
