package com.osia.osia_erp.dto.calendar.v1

import com.osia.osia_erp.dto.BaseDto
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarDto : BaseDto() {
    var outOfMonth: Boolean = false
    var dayNumber: Int? = null
    var dayOfWeek: DayOfWeek? = null
    var totalDay: LocalDate? = null
    var tasks: List<CalendarTaskDto> = mutableListOf()
}
