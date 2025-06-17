package com.osia.ampirux.dto.barbershop.v1

import com.osia.ampirux.dto.BaseDto
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class BarberShopDto : BaseDto() {
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

