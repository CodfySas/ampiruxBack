package com.osia.ampirux.dto.productcategory.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class ProductCategoryDto : BaseDto() {
    var name: String? = null
    var barbershopUuid: UUID? = null
}
