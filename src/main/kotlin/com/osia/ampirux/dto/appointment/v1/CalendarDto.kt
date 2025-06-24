package com.osia.ampirux.dto.appointment.v1

import com.osia.ampirux.dto.BaseDto
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class CalendarDto : BaseDto() {
    var outOfMonth: Boolean = false
    var dayNumber: Int? = null
    var dayOfWeek: DayOfWeek? = null
    var totalDay: LocalDateTime? = null
    var tasks: List<AppointmentDto> = mutableListOf()
}
