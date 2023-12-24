package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.judgment.JudgmentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "judgments",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        JudgmentListener::class
    ]
)
@Where(clause = "deleted = false")
data class Judgment(
    var name: String? = null,
    var uuidClassroomStudent: UUID? = null,
    var uuidSubject: UUID? = null,
    var uuidStudent: UUID? = null,
    var period: Int? = null
) : BaseModel()
