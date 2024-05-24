package com.osia.nota_maestro.dto.postReact.v1

import java.util.UUID

class PostReactRequest {
    var uuidUser: UUID? = null
    var react: Int? = null
    var uuidPost: UUID? = null
    var uuidComment: UUID? = null
}
