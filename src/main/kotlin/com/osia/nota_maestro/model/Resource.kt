package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "resources",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AuditingEntityListener::class,
    ]
)
@Where(clause = "deleted = false")
data class Resource(
    @NotNull
    var uuidSchool: UUID? = null,
    var name: String? = null,
) : BaseModel()
