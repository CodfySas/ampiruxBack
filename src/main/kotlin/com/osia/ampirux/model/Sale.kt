package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.sale.SaleListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "sale",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SaleListener::class
    ]
)
@Where(clause = "deleted = false")
data class Sale(
    var clientUuid: UUID? = null,
    var barberUuid: UUID? = null,
    var date: LocalDateTime? = null,
    var status: String? = null,
    var paymentMethod: String? = null,
    var isCourtesy: Boolean? = null,
    var total: BigDecimal? = null,
    var discountUuid: UUID? = null,
    var barbershopUuid: UUID? = null
) : BaseModel()
