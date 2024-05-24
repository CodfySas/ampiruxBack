package com.osia.nota_maestro.dto.postReact.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PostReactDto : BaseDto() {
    var uuidUser: UUID? = null
    var react: Int = 0
    var uuidPost: UUID? = null
    var uuidComment: UUID? = null

    var userName: String = ""
    var userRole: String = ""
    var userClassroom: String? = null
}
