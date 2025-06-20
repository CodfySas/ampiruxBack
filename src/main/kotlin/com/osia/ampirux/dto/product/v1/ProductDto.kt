package com.osia.ampirux.dto.product.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.productcategory.v1.ProductCategoryDto
import java.math.BigDecimal
import java.util.UUID

class ProductDto : BaseDto() {
    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var stock: Double? = null
    var unit: String? = null
    var categoryUuid: UUID? = null
    var category: ProductCategoryDto? = null
    var barbershopUuid: UUID? = null
    var sizePerUnit: Int? = null
    var remainUnit: Int? = null
}
