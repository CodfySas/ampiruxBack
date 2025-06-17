package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.expense.ExpenseListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "expenses",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ExpenseListener::class
    ]
)
@Where(clause = "deleted = false")
data class Expense(
    var cashUuid: UUID? = null,
    var category: String? = null,
    var amount: BigDecimal? = null,
    var notes: String? = null
) : BaseModel()
