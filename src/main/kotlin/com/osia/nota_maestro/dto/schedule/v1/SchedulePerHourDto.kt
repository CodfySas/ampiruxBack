package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseDto

class SchedulePerHourDto : BaseDto() {
    var init: String? = null
    var finish: String? = null
    var schedules: List<ScheduleDto>? = null
}
