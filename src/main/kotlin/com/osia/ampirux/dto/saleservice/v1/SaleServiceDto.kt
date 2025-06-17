package com.osia.ampirux.dto.saleservice.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductDto
import java.math.BigDecimal
import java.util.UUID

class SaleServiceDto : BaseDto() {
    var saleUuid: UUID? = null
    var serviceUuid: UUID? = null
    var price: BigDecimal? = null
    var isCourtesy: Boolean? = null
    var commissionRate: Double? = null
    var barberUuid: UUID? = null
    var usedProducts: List<SaleServiceProductDto>? = null
}
