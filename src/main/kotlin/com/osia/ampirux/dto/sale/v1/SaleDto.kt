package com.osia.ampirux.dto.sale.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.barber.v1.BarberDto
import com.osia.ampirux.dto.client.v1.ClientDto
import com.osia.ampirux.dto.saleproduct.v1.SaleProductDto
import com.osia.ampirux.dto.saleservice.v1.SaleServiceDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class SaleDto : BaseDto() {
    var clientUuid: UUID? = null
    var barberUuid: UUID? = null
    var date: LocalDateTime? = null
    var status: String? = null
    var paymentMethod: String? = null
    var isCourtesy: Boolean? = null
    var total: BigDecimal? = null
    var services: List<SaleServiceDto>? = null
    var products: List<SaleProductDto>? = null
    var discountUuid: UUID? = null
    var barbershopUuid: UUID? = null

    var client: ClientDto? = null
    var barber: BarberDto? = null

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
