package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.classroomResource.ClassroomResourceListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "classroom_resources",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClassroomResourceListener::class
    ]
)
@Where(clause = "deleted = false")
data class ClassroomResource(
    var name: String? = null,
    var type: String? = null,
    var description: String? = null,
    var classroom: UUID? = null,
    var subject: UUID? = null,
    var period: Int? = null,
    var finishTime: LocalDate? = null,
    var durationTime: Int? = null,
    var lastHour: String? = null,
    var hasFile: Boolean? = false,
    var ext: String? = null
) : BaseModel()
