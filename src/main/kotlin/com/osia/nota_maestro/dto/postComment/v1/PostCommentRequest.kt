package com.osia.nota_maestro.dto.postComment.v1

import java.util.UUID

class PostCommentRequest {
    var uuidUser: UUID? = null
    var responses: Int? = null
    var description: String? = null
    var likes: Int? = null
    var loved: Int? = null
    var wows: Int? = null
    var interesting: Int? = null
    var dislikes: Int? = null
    var reacts: Int? = null
    var uuidPost: UUID? = null
    var isResponse: Boolean? = null
    var uuidParent: UUID? = null
}
