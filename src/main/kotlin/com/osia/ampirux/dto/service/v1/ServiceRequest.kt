package com.osia.ampirux.dto.service.v1

import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductRequest
import java.math.BigDecimal
import java.util.UUID

class ServiceRequest {
    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var durationMinutes: Int? = null
    var popularity: Int? = null
    var defaultProducts: List<ServiceDefaultProductRequest>? = null
    var barbershopUuid: UUID? = null
}
