package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ScheduleDto : BaseDto() {
    var minPerHour: Int? = null
    var name: String? = null
    var uuidClassroomStudent: UUID? = null
    var uuidSubject: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
}
