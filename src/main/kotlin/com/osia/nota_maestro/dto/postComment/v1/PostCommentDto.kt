package com.osia.nota_maestro.dto.postComment.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PostCommentDto : BaseDto() {
    var uuidUser: UUID? = null
    var responses: Int = 0
    var description: String = ""
    var likes: Int = 0
    var loved: Int = 0
    var wows: Int = 0
    var interesting: Int = 0
    var dislikes: Int = 0
    var reacts: Int = 0
    var uuidPost: UUID? = null
    var isResponse: Boolean = false
    var uuidParent: UUID? = null
    var selectedReact: Int? = 0

    var userName: String = ""
    var userRole: String = ""
    var userClassroom: String? = null
}
