package com.osia.ampirux.dto.discountservice.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class DiscountServiceDto : BaseDto() {
    var discountUuid: UUID? = null
    var serviceUuid: UUID? = null
}
