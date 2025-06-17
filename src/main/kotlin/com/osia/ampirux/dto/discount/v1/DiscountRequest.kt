package com.osia.ampirux.dto.discount.v1

import com.osia.ampirux.dto.discountproduct.v1.DiscountProductRequest
import com.osia.ampirux.dto.discountservice.v1.DiscountServiceRequest
import java.time.LocalDateTime
import java.util.UUID

class DiscountRequest {
    var name: String? = null
    var description: String? = null
    var initPromotion: LocalDateTime? = null
    var finishPromotion: LocalDateTime? = null
    var percentage: Double? = null
    var appliesTo: String? = null // SERVICES, PRODUCTS, ALL, PERSONALIZED
    var services: List<DiscountServiceRequest>? = null // ONLY IN PERSONALIZED
    var products: List<DiscountProductRequest>? = null // ONLY IN PERSONALIZED
    var costAssumption: String? = null // BUSINESS, BARBER, BOTH
    var barbershopUuid: UUID? = null
}
