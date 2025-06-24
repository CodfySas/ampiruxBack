package com.osia.ampirux.dto.servicedefaultproduct.v1

import java.math.BigDecimal
import java.util.UUID

class ServiceDefaultProductRequest {
    var uuid: UUID? = null
    var serviceUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var unit: String? = null // "ml", "g", "units", etc.
    var costType: String? = null // "client", "courtesy", "barber"
    var price: BigDecimal? = null
}
