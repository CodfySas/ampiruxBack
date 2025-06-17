package com.osia.ampirux.service.expense

import com.osia.ampirux.dto.expense.v1.ExpenseDto
import com.osia.ampirux.dto.expense.v1.ExpenseRequest
import com.osia.ampirux.model.Expense
import com.osia.ampirux.service.common.CommonService

interface ExpenseService : CommonService<Expense, ExpenseDto, ExpenseRequest>
