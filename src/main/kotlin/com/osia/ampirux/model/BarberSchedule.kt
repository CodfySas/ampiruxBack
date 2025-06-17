package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.barberSchedule.BarberScheduleListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "barber_schedules",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        BarberScheduleListener::class
    ]
)
@Where(clause = "deleted = false")
data class BarberSchedule(
    var barberUuid: UUID? = null,
    var dayOfWeek: Int? = null, // 0 = sunday
    var active: Boolean? = null,
    var startTime: LocalTime? = null,
    var endTime: LocalTime? = null
) : BaseModel()
