package com.osia.osia_erp.model

import com.osia.osia_erp.model.abstracts.BaseModel
import com.osia.osia_erp.model.listener.user.UserListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

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
    var role: String? = null,
    @NotNull
    var uuidCompany: UUID? = null
) : BaseModel()
