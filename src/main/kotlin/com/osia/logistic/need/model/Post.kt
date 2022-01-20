package com.osia.logistic.need.model

import com.osia.logistic.need.model.abstracts.BaseModel
import com.osia.logistic.need.model.listener.post.PostListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.envers.Audited
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table
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

) : BaseModel() {

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var user: User? = null
}
