package com.osia.ampirux.dto.saleserviceproduct.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.product.v1.ProductDto
import java.math.BigDecimal
import java.util.UUID

class SaleServiceProductDto : BaseDto() {
    var saleServiceUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var unit: String? = null
    var costType: String? = null
    var price: BigDecimal? = null
    var product: ProductDto? = null
}
