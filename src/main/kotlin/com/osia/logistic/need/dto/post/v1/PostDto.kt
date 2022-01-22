package com.osia.logistic.need.dto.post.v1

import com.osia.logistic.need.dto.BaseDto
import com.osia.logistic.need.dto.client.v1.ClientDto

open class PostDto : BaseDto() {
    var description: String = ""
    var comments: Int = 0
    var clientDto: ClientDto = ClientDto()
}
