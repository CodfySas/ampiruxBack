package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.user.UserListener
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
    var dni: String? = null,
    var documentType: String? = null,
    var lastname: String? = null,
    var role: String? = null,
    var active: Boolean? = true,
    var superUser: Boolean? = false,
    @NotNull
    var uuidSchool: UUID? = null,
    var uuidRole: UUID? = null,
    var phone: String? = null,
    var address: String? = null,
    var email: String? = null,
    var actualGrade: UUID? = null,
    var parentName: String? = null,
    var parentPhone: String? = null,
    var parentEmail: String? = null,
    var parentAddress: String? = null,
    var parentVulnerability: String? = null

) : BaseModel()
