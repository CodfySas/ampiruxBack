package com.osia.ampirux.dto.barber.v1

import com.osia.ampirux.dto.barberschedule.v1.BarberScheduleDto
import com.osia.ampirux.dto.barberschedule.v1.BarberScheduleRequest
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class BarberRequest {
    var name: String? = null
    var address: String? = null
    var phone: String? = null
    var email: String? = null
    var barbershopUuid: UUID? = null
    var dni: String? = null
    var position: String? = null
    var hireDate: LocalDate? = null
    var baseSalary: BigDecimal? = null
    var commissionRate: Double? = null
    var status: String? = null
    var schedule: List<BarberScheduleDto>? = null
}
