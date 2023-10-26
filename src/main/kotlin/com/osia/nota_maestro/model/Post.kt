package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.post.PostListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.envers.Audited
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "posts",
)
@Entity
@DynamicUpdate
@Audited
@EntityListeners(
    value = [
        PostListener::class,
    ]
)
data class Post(

    var description: String = "",
    var comments: Int = 0,

) : BaseModel()
