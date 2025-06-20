package com.osia.ampirux.dto.product.v1

import java.math.BigDecimal
import java.util.UUID

class ProductRequest {
    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var stock: Double? = null
    var unit: String? = null
    var categoryUuid: UUID? = null
    var barbershopUuid: UUID? = null
    var sizePerUnit: Int? = null
    var remainUnit: Int? = null
}
