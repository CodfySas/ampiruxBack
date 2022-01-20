package com.osia.logistic.need.model

import com.osia.logistic.need.model.abstracts.BaseModel
import com.osia.logistic.need.model.listener.user.UserListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.envers.Audited
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table
@Entity
@DynamicUpdate
@Audited
@EntityListeners(
    value = [
        UserListener::class,
    ]
)
data class User(
    var name: String = "",
    var email: String = "",
    var password: String = ""
) : BaseModel()
