package com.osia.ampirux.dto.discountproduct.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class DiscountProductDto : BaseDto() {
    var discountUuid: UUID? = null
    var productUuid: UUID? = null
}
