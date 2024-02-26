package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseDto

class ScheduleComplete : BaseDto() {
    var hours: List<SchedulePerHourDto>? = null
    var hourInit: String? = null
    var hourFinish: String? = null
    var recessInit: String? = null
    var recessFinish: String? = null
    var recessaInit: String? = null
    var recessaFinish: String? = null
    var duration: Int? = null
    var recess: Int? = null
}
