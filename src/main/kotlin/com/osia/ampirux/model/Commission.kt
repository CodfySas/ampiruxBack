package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.commission.CommissionListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "commission",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        CommissionListener::class
    ]
)
@Where(clause = "deleted = false")
data class Commission(
    var barberUuid: UUID? = null,
    var saleServiceUuid: UUID? = null,
    var amount: BigDecimal? = null,
    var status: String? = null,
    var paidAt: LocalDateTime? = null
) : BaseModel()
