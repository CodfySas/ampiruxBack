package com.osia.nota_maestro.service.calendarTask

import com.osia.nota_maestro.dto.calendar.v1.CalendarDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskRequest
import java.time.LocalDate
import java.util.UUID

interface CalendarTaskService {
    fun getCalendarMonth(year: Int = LocalDate.now().year, month: Int = LocalDate.now().month.value, school: UUID): List<List<CalendarDto>>
    fun getCalendarDay(year: Int = LocalDate.now().year, month: Int = LocalDate.now().monthValue, day: Int = LocalDate.now().dayOfMonth, school: UUID): CalendarDto
    fun submitTask(school: UUID, calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto
    fun updateTask(school: UUID, uuid: UUID, calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto
}
