package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.preliminary.PreliminaryListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "preliminaries",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        PreliminaryListener::class
    ]
)
@Where(clause = "deleted = false")
data class Preliminary(
    var target: String? = null,
    var success: Boolean? = null,
    var aspect: String? = null,
    var observations: String? = null,
    var uuidClassroom: UUID? = null,
    var uuidSubject: UUID? = null,
    var uuidStudent: UUID? = null,
    var period: Int? = null
) : BaseModel()
