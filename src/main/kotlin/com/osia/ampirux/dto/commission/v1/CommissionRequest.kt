package com.osia.ampirux.dto.commission.v1

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class CommissionRequest {
    var barberUuid: UUID? = null
    var saleServiceUuid: UUID? = null
    var amount: BigDecimal? = null
    var status: String? = null
    var paidAt: LocalDateTime? = null
}
