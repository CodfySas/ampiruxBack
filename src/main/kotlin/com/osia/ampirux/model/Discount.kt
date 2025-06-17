package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.discount.DiscountListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "discounts",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DiscountListener::class
    ]
)
@Where(clause = "deleted = false")
data class Discount(
    var name: String? = null,
    var description: String? = null,
    var initPromotion: LocalDateTime? = null,
    var finishPromotion: LocalDateTime? = null,
    var percentage: Double? = null,
    var appliesTo: String? = null, // SERVICES, PRODUCTS, ALL, PERSONALIZED
    var costAssumption: String? = null, // BUSINESS, BARBER, BOTH
    var barbershopUuid: UUID? = null
) : BaseModel()
