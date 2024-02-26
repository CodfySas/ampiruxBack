package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.attendanceFail.AttendanceFailListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "attendance_fails",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AttendanceFailListener::class
    ]
)
@Where(clause = "deleted = false")
data class AttendanceFail(
    var reason: String? = null,
    var uuidAttendance: UUID? = null,
    var uuidStudent: UUID? = null
) : BaseModel()
