package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.enums.UserType
import com.osia.nota_maestro.model.listener.user.UserListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    var dni: String? = null,
    var documentType: String? = null,
    var lastname: String? = null,
    @Enumerated(EnumType.STRING)
    var role: UserType? = null,
    @NotNull
    var uuidSchool: UUID? = null,
    var uuidRole: UUID? = null
) : BaseModel()
