package com.osia.ampirux.dto.servicedefaultproduct.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.product.v1.ProductDto
import java.math.BigDecimal
import java.util.UUID

class ServiceDefaultProductDto : BaseDto() {
    var serviceUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var unit: String? = null // "ml", "g", "units", etc.
    var costType: String? = null // "client", "courtesy", "barber"
    var product: ProductDto? = null
    var price: BigDecimal? = null
}
