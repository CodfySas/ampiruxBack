package com.osia.nota_maestro.dto.post.v1

import com.osia.nota_maestro.dto.BaseDto

open class PostDto : BaseDto() {
    var description: String = ""
    var comments: Int = 0
}
