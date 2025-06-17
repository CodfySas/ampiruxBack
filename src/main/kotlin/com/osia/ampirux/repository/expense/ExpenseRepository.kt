package com.osia.ampirux.repository.expense
import com.osia.ampirux.model.Expense
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("expenses.crud_repository")
interface ExpenseRepository : CommonRepository<Expense>
