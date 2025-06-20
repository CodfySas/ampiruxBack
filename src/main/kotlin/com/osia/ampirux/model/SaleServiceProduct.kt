package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.saleserviceproduct.SaleServiceProductListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "sale_service_products",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SaleServiceProductListener::class
    ]
)
@Where(clause = "deleted = false")
data class SaleServiceProduct(
    var saleServiceUuid: UUID? = null,
    var productUuid: UUID? = null,
    var quantity: Double? = null,
    var unit: String? = null,
    var costType: String? = null,
    var price: BigDecimal? = null,
) : BaseModel()
