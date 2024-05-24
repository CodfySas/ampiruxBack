package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.postComment.PostCommentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "post_comments",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        PostCommentListener::class
    ]
)
@Where(clause = "deleted = false")
data class PostComment(
    var uuidUser: UUID? = null,
    var responses: Int = 0,
    var description: String = "",
    var likes: Int = 0,
    var loved: Int = 0,
    var wows: Int = 0,
    var interesting: Int = 0,
    var dislikes: Int = 0,
    var reacts: Int = 0,
    var uuidPost: UUID? = null,
    var isResponse: Boolean = false,
    var uuidParent: UUID? = null
) : BaseModel()
