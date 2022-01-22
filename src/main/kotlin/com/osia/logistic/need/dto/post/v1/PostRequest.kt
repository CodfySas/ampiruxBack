package com.osia.logistic.need.dto.post.v1

import java.util.UUID
import javax.validation.constraints.NotNull

open class PostRequest {
    var description: String = ""
    var comments: Int = 0
    @NotNull
    var clientUuid: UUID? = null
}
