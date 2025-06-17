package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.product.ProductListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "products",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ProductListener::class
    ]
)
@Where(clause = "deleted = false")
data class Product(
    var name: String? = null,
    var description: String? = null,
    var price: BigDecimal? = null,
    var stock: Double? = null,
    var unit: String? = null,
    var categoryUuid: UUID? = null,
    var barbershopUuid: UUID? = null
) : BaseModel()
