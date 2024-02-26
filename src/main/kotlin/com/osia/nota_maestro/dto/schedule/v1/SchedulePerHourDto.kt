package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalTime
import java.util.UUID

class SchedulePerHourDto : BaseDto() {
    var init: String? = null
    var finish: String? = null
    var schedules: List<ScheduleDto>? = null
}
