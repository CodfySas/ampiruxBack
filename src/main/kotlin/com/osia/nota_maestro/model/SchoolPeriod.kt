package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.schoolPeriod.SchoolPeriodListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "school_periods",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SchoolPeriodListener::class
    ]
)
@Where(clause = "deleted = false")
data class SchoolPeriod(
    var number: Int? = null,
    var init: LocalDateTime? = null,
    var finish: LocalDateTime? = null,
    var uuidSchool: UUID? = null
) : BaseModel()
