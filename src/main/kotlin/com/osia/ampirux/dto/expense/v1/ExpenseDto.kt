package com.osia.ampirux.dto.expense.v1

import com.osia.ampirux.dto.BaseDto
import java.math.BigDecimal
import java.util.UUID

class ExpenseDto : BaseDto() {
    var cashUuid: UUID? = null
    var category: String? = null
    var amount: BigDecimal? = null
    var notes: String? = null
}
