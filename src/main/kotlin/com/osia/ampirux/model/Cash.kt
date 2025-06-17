package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.cash.CashListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "cashs",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        CashListener::class
    ]
)
@Where(clause = "deleted = false")
data class Cash(
    var barbershopUuid: UUID? = null,
    var date: LocalDate? = null,
    var openedAt: LocalDateTime? = null,
    var closedAt: LocalDateTime? = null,
    var baseAmount: BigDecimal? = null,
    var status: String? = null,
    var cashSales: BigDecimal? = null,
    var cardSales: BigDecimal? = null,
    var transferSales: BigDecimal? = null,
) : BaseModel()
