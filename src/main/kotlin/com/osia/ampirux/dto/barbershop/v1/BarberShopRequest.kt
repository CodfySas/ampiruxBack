package com.osia.ampirux.dto.barbershop.v1

import java.math.BigDecimal
import java.util.UUID

class BarberShopRequest {
    var name: String? = null
    var nit: String? = null
    var address: String? = null
    var phone: String? = null
    var email: String? = null
    var logoUrl: String? = null
    var defaultCashBase: BigDecimal? = null
    var ivaPercent: Double? = null
    var applyIva: Boolean? = null
    var retentionPercent: Double? = null
}


