package com.osia.nota_maestro.dto.postReact.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PostReactComplete : BaseDto() {
    var react: String = ""
    var count: Int = 0
    var reacts: List<PostReactDto> = mutableListOf()
    var img: String = ""
}
