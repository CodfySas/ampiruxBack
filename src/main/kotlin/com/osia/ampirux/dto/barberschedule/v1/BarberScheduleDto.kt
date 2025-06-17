package com.osia.ampirux.dto.barberschedule.v1

import com.osia.ampirux.dto.BaseDto
import java.time.LocalTime
import java.util.UUID

class BarberScheduleDto : BaseDto() {
    var barberUuid: UUID? = null
    var dayOfWeek: Int? = null // 0 = sunday
    var active: Boolean? = null
    var startTime: LocalTime? = null
    var endTime: LocalTime? = null
}
