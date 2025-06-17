package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.appointment.AppointmentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "appointments",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AppointmentListener::class
    ]
)
@Where(clause = "deleted = false")
data class Appointment(
    var clientUuid: UUID? = null,
    var barberUuid: UUID? = null,
    var serviceUuid: UUID? = null,
    var scheduledAt: LocalDateTime? = null,
    var status: String? = null, // "pending", "confirmed", "completed", "cancelled"
    var notes: String? = null,
    var barbershopUuid: UUID? = null,
) : BaseModel()
