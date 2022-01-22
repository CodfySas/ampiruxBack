package com.osia.logistic.need.model

import com.osia.logistic.need.model.abstracts.BaseModel
import com.osia.logistic.need.model.listener.client.ClientListener
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
        ClientListener::class,
    ]
)
data class Client(
    var name: String = "",
    var email: String = "",
    var password: String = ""
) : BaseModel()
