package com.osia.osia_erp.dto.post.v1

import com.osia.osia_erp.dto.BaseDto

open class PostDto : BaseDto() {
    var description: String = ""
    var comments: Int = 0
}
