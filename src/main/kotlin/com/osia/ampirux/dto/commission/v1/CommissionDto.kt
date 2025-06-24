package com.osia.ampirux.dto.commission.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.sale.v1.SaleDto
import com.osia.ampirux.dto.service.v1.ServiceDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class CommissionDto : BaseDto() {
    var barberUuid: UUID? = null
    var saleServiceUuid: UUID? = null
    var amount: BigDecimal? = null
    var status: String? = null
    var paidAt: LocalDateTime? = null
    var service: ServiceDto? = null
    var sale: SaleDto? = null
}
