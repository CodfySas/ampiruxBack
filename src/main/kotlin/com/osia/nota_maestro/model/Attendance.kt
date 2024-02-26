package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.attendance.AttendanceListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "attendances",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AttendanceListener::class
    ]
)
@Where(clause = "deleted = false")
data class Attendance(
    var uuidSchool: UUID? = null,
    var uuidClassroom: UUID? = null,
    var day: Int? = null,
    var month: Int? = null,
    var uuidSubject: UUID? = null
) : BaseModel()
