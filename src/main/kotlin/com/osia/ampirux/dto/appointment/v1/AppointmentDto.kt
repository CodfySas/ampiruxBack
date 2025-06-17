package com.osia.ampirux.dto.appointment.v1

import com.osia.ampirux.dto.BaseDto
import java.time.LocalDateTime
import java.util.UUID

class AppointmentDto : BaseDto() {
    var clientUuid: UUID? = null
    var barberUuid: UUID? = null
    var serviceUuid: UUID? = null
    var scheduledAt: LocalDateTime? = null
    var status: String? = null // "pending", "confirmed", "completed", "cancelled"
    var notes: String? = null
    var barbershopUuid: UUID? = null
}
