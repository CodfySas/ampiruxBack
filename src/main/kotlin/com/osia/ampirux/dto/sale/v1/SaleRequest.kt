package com.osia.ampirux.dto.sale.v1

import com.osia.ampirux.dto.barber.v1.BarberRequest
import com.osia.ampirux.dto.saleproduct.v1.SaleProductRequest
import com.osia.ampirux.dto.saleservice.v1.SaleServiceRequest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class SaleRequest {
    var clientUuid: UUID? = null
    var barberUuid: UUID? = null
    var barber: BarberRequest? = null
    var date: LocalDateTime? = null
    var status: String? = null
    var paymentMethod: String? = null
    var isCourtesy: Boolean? = null
    var total: BigDecimal? = null
    var services: List<SaleServiceRequest>? = null
    var products: List<SaleProductRequest>? = null
    var discountUuid: UUID? = null
    var barbershopUuid: UUID? = null

    var subtotalServices: BigDecimal? = null
    var commissions: BigDecimal? = null
    var commissionDiscount: BigDecimal? = null
    var subtotalServiceProducts: BigDecimal? = null
    var subtotalProducts: BigDecimal? = null
    var discountPercent: BigDecimal? = null
    var subtotalDiscount: BigDecimal? = null

    var hasIva: Boolean? = null
    var ivaService: Boolean? = null
    var ivaProduct: Boolean? = null
    var iva: BigDecimal? = null

    var subtotalServicesIva: BigDecimal? = null
    var subtotalProductsIva: BigDecimal? = null
    var subtotalServiceProductsIva: BigDecimal? = null
}
