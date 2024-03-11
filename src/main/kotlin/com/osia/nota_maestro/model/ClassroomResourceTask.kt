package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.classroomResourceTask.ClassroomResourceTaskListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "classroom_resource_tasks",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClassroomResourceTaskListener::class
    ]
)
@Where(clause = "deleted = false")
data class ClassroomResourceTask(
    var uuidClassroomResource: UUID? = null,
    var uuidStudent: UUID? = null,
    var uuidClassroomStudent: UUID? = null,
    var name: String? = null,
    var ext: String? = null,
    var hasFile: Boolean? = null,
    var description: String? = null,
    var submitAt: LocalDate? = null,
    var submitAtHour: String? = null,
    var note: Double? = null,
    var observation: String? = null
) : BaseModel()
