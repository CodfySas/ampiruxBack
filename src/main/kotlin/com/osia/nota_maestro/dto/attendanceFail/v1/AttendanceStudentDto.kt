package com.osia.nota_maestro.dto.attendanceFail.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

class AttendanceStudentDto : BaseDto() {
    var outOfMonth: Boolean = false
    var dayNumber: Int? = null
    var dayOfWeek: DayOfWeek? = null
    var totalDay: LocalDate? = null

    var enabled: Boolean? = null
    var failed: Boolean? = null
    var reason: String? = null
}
