package com.osia.ampirux.service.appointment

import com.osia.ampirux.dto.appointment.v1.AppointmentDto
import com.osia.ampirux.dto.appointment.v1.AppointmentRequest
import com.osia.ampirux.model.Appointment
import com.osia.ampirux.service.common.CommonService

interface AppointmentService : CommonService<Appointment, AppointmentDto, AppointmentRequest>
