package com.osia.ampirux.dto.service.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductDto
import java.math.BigDecimal
import java.util.UUID

class ServiceDto : BaseDto() {
    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var durationMinutes: Int? = null
    var popularity: Int? = null
    var defaultProducts: List<ServiceDefaultProductDto>? = null
    var barbershopUuid: UUID? = null
}
