package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.planning.PlanningListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "plannings",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        PlanningListener::class
    ]
)
@Where(clause = "deleted = false")
data class Planning(
    var day: String? = null,
    var position: Int? = null,

    var area: String? = null,
    var goals: String? = null,
    var topic: String? = null,
    var activity: String? = null,
    var resources: String? = null,

    var classroom: UUID? = null,
    var subject: UUID? = null,
    var uuidTeacher: UUID? = null,
    var week: Int? = null,

    var dateRange: String? = null,
    var observation: String? = "",
    var status: String? = "pending",
    var userReview: UUID? = null
) : BaseModel()
