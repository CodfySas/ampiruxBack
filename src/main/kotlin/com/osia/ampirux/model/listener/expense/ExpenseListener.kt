package com.osia.ampirux.model.listener.expense

import com.osia.ampirux.model.Expense
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.expense.ExpenseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ExpenseListener : CodeSetter() {

    companion object {
        private lateinit var expensesRepository: ExpenseRepository
    }

    @Autowired
    fun setProducer(_expensesRepository: ExpenseRepository) {
        expensesRepository = _expensesRepository
    }

    @PrePersist
    fun prePersist(expenses: Expense) {
        this.setCode(expensesRepository, expenses)
    }
}
