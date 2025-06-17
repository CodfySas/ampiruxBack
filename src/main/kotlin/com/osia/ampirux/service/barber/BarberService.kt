package com.osia.ampirux.service.barber

import com.osia.ampirux.dto.barber.v1.BarberDto
import com.osia.ampirux.dto.barber.v1.BarberRequest
import com.osia.ampirux.model.Barber
import com.osia.ampirux.service.common.CommonService

interface BarberService : CommonService<Barber, BarberDto, BarberRequest>
