package com.osia.ampirux.dto.product.v1

import com.osia.ampirux.dto.BaseDto
import java.math.BigDecimal
import java.util.UUID

class ProductDto : BaseDto() {
    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var stock: Double? = null
    var unit: String? = null
    var categoryUuid: UUID? = null
    var barbershopUuid: UUID? = null
}
