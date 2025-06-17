package com.osia.ampirux.dto.expense.v1

import java.math.BigDecimal
import java.util.UUID

class ExpenseRequest {
    var cashUuid: UUID? = null
    var category: String? = null
    var amount: BigDecimal? = null
    var notes: String? = null
}
