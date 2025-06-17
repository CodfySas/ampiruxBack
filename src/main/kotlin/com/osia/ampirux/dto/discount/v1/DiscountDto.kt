package com.osia.ampirux.dto.discount.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.discountproduct.v1.DiscountProductDto
import com.osia.ampirux.dto.discountservice.v1.DiscountServiceDto
import java.time.LocalDateTime
import java.util.UUID

class DiscountDto : BaseDto() {
    var name: String? = null
    var description: String? = null
    var initPromotion: LocalDateTime? = null
    var finishPromotion: LocalDateTime? = null
    var percentage: Double? = null
    var appliesTo: String? = null // SERVICES, PRODUCTS, ALL, PERSONALIZED
    var services: List<DiscountServiceDto>? = null // ONLY IN PERSONALIZED
    var products: List<DiscountProductDto>? = null // ONLY IN PERSONALIZED
    var costAssumption: String? = null // BUSINESS, BARBER, BOTH
    var barbershopUuid: UUID? = null
}
