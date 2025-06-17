package com.osia.ampirux.dto.cash.v1

import com.osia.ampirux.dto.commission.v1.CommissionRequest
import com.osia.ampirux.dto.expense.v1.ExpenseRequest
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class CashRequest {
    var barbershopUuid: UUID? = null
    var date: LocalDate? = null
    var openedAt: LocalDateTime? = null
    var closedAt: LocalDateTime? = null
    var baseAmount: BigDecimal? = null
    var status: String? = null
    var cashSales: BigDecimal? = null
    var cardSales: BigDecimal? = null
    var transferSales: BigDecimal? = null
    var expenses: List<ExpenseRequest>? = null
    var commissions: List<CommissionRequest>? = null
}
