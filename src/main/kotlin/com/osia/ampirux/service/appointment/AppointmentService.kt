package com.osia.ampirux.service.appointment

import com.osia.ampirux.dto.appointment.v1.AppointmentDto
import com.osia.ampirux.dto.appointment.v1.AppointmentRequest
import com.osia.ampirux.dto.appointment.v1.CalendarDto
import com.osia.ampirux.model.Appointment
import com.osia.ampirux.service.common.CommonService
import java.time.LocalDate
import java.util.UUID

interface AppointmentService : CommonService<Appointment, AppointmentDto, AppointmentRequest> {
    fun getCalendarMonth(year: Int = LocalDate.now().year, month: Int = LocalDate.now().month.value, school: UUID): List<List<CalendarDto>>
    fun getCalendarDay(year: Int = LocalDate.now().year, month: Int = LocalDate.now().monthValue, day: Int = LocalDate.now().dayOfMonth, school: UUID): CalendarDto
}
