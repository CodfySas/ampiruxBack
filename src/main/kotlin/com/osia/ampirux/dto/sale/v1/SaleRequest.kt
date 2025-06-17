package com.osia.ampirux.dto.sale.v1

import com.osia.ampirux.dto.saleproduct.v1.SaleProductRequest
import com.osia.ampirux.dto.saleservice.v1.SaleServiceRequest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class SaleRequest {
    var clientUuid: UUID? = null
    var barberUuid: UUID? = null
    var date: LocalDateTime? = null
    var status: String? = null
    var paymentMethod: String? = null
    var isCourtesy: Boolean? = null
    var total: BigDecimal? = null
    var services: List<SaleServiceRequest>? = null
    var products: List<SaleProductRequest>? = null
    var discountUuid: UUID? = null
    var barbershopUuid: UUID? = null
}
