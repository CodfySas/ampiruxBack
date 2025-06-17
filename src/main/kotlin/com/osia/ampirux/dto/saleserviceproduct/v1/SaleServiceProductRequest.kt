package com.osia.ampirux.dto.saleserviceproduct.v1

import java.util.UUID

class SaleServiceProductRequest {
    var saleServiceUuid: UUID? = null
    var productUuid: UUID? = null
    var quantity: Double? = null
    var unit: String? = null
    var costType: String? = null
}
