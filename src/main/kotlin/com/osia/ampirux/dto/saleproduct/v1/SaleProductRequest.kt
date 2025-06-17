package com.osia.ampirux.dto.saleproduct.v1

import java.math.BigDecimal
import java.util.UUID

class SaleProductRequest {
    var saleUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var price: BigDecimal? = null
    var total: BigDecimal? = null
}
