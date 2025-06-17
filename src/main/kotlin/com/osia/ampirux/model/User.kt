package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.user.UserListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "users",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        UserListener::class
    ]
)
@Where(clause = "deleted = false")
data class User(
    var username: String? = null,
    var password: String? = null,
    var name: String? = null,
    var lastname: String? = null,
    var role: String? = null,
    var active: Boolean? = true,
    var phone: String? = null,
    var email: String? = null,
    var image: String? = null,
    var barbershopUuid: UUID? = null
) : BaseModel()
