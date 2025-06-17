package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.discountservice.DiscountServiceListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "discount_services",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DiscountServiceListener::class
    ]
)
@Where(clause = "deleted = false")
data class DiscountService(
    var discountUuid: UUID? = null,
    var serviceUuid: UUID? = null
) : BaseModel()
