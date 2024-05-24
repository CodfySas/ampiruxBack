package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.postReact.PostReactListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "post_reacts",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        PostReactListener::class
    ]
)
@Where(clause = "deleted = false")
data class PostReact(
    var uuidUser: UUID? = null,
    var react: Int = 0,
    var uuidPost: UUID? = null,
    var uuidComment: UUID? = null
) : BaseModel()
