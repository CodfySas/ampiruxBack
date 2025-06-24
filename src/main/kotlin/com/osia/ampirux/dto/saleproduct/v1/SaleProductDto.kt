package com.osia.ampirux.dto.saleproduct.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.product.v1.ProductDto
import java.math.BigDecimal
import java.util.UUID

class SaleProductDto : BaseDto() {
    var saleUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var price: BigDecimal? = null
    var total: BigDecimal? = null
    var product: ProductDto? = null
}
