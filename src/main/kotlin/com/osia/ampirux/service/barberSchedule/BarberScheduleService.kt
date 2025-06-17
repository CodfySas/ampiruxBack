package com.osia.ampirux.service.barberSchedule

import com.osia.ampirux.dto.barberschedule.v1.BarberScheduleDto
import com.osia.ampirux.dto.barberschedule.v1.BarberScheduleRequest
import com.osia.ampirux.model.BarberSchedule
import com.osia.ampirux.service.common.CommonService

interface BarberScheduleService : CommonService<BarberSchedule, BarberScheduleDto, BarberScheduleRequest>
