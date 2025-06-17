package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.discountproduct.DiscountProductListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "discount_products",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DiscountProductListener::class
    ]
)
@Where(clause = "deleted = false")
data class DiscountProduct(
    var discountUuid: UUID? = null,
    var productUuid: UUID? = null
) : BaseModel()
