package com.osia.nota_maestro.dto.post.v1

import java.util.UUID

class PostRequest {
    var description: String? = null
    var comments: Int? = null
    var uuidUser: UUID? = null
    var likes: Int? = null
    var loved: Int? = null
    var wows: Int? = null
    var interesting: Int? = null
    var dislikes: Int? = null
    var reacts: Int? = null
    var pinned: Boolean? = null
    var type: String? = null
    var classroom: UUID? = null
    var uuidSchool: UUID? = null
}
