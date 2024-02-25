package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.schedule.ScheduleListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "schedules",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ScheduleListener::class
    ]
)
@Where(clause = "deleted = false")
data class Schedule(
    var init: String? = null,
    var finish: String? = null,
    var dayOfWeek: Int? = null,
    var uuidGradeSubject: UUID? = null,
    var uuidClassroomSubject: UUID? = null,
    var uuidSchool: UUID? = null,
    var uuidClassroom: UUID? = null
) : BaseModel()
