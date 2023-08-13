package com.osia.osia_erp.service.calendarTask

import com.osia.osia_erp.dto.calendar.v1.CalendarDto
import java.time.LocalDate

interface CalendarTaskService {
    fun getCalendarMonth(year: Int = LocalDate.now().year, month: Int = LocalDate.now().month.value): List<List<CalendarDto>>
    fun getCalendarDay(year: Int = LocalDate.now().year, month: Int = LocalDate.now().monthValue, day: Int = LocalDate.now().dayOfMonth):CalendarDto
}
