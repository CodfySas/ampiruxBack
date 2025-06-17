package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.saleservice.SaleServiceListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "sale_services",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SaleServiceListener::class
    ]
)
@Where(clause = "deleted = false")
data class SaleService(
    var saleUuid: UUID? = null,
    var serviceUuid: UUID? = null,
    var price: BigDecimal? = null,
    var isCourtesy: Boolean? = null,
    var commissionRate: Double? = null,
    var barberUuid: UUID? = null,
) : BaseModel()
