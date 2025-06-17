package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.saleproduct.SaleProductListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "sale_products",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SaleProductListener::class
    ]
)
@Where(clause = "deleted = false")
data class SaleProduct(
    var saleUuid: UUID? = null,
    var productUuid: UUID? = null,
    var quantity: Double? = null,
    var price: BigDecimal? = null,
    var total: BigDecimal? = null
) : BaseModel()
