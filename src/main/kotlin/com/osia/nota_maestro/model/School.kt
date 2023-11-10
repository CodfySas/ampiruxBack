package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.school.SchoolListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "schools",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SchoolListener::class
    ]
)
@Where(clause = "deleted = false")
data class School(
    var name: String? = null,
    var color1: String? = "E78617",
    var color2: String? = "e35205",
    var active: Boolean? = true,
    var expireDate: LocalDateTime? = null
) : BaseModel()
