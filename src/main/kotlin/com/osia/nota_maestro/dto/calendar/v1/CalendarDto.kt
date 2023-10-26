package com.osia.nota_maestro.dto.calendar.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarDto : BaseDto() {
    var outOfMonth: Boolean = false
    var dayNumber: Int? = null
    var dayOfWeek: DayOfWeek? = null
    var totalDay: LocalDate? = null
    var tasks: List<CalendarTaskDto> = mutableListOf()
}
