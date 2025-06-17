package com.osia.ampirux.dto.expense.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Expense
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
)
interface ExpenseMapper : BaseMapper<ExpenseRequest, Expense, ExpenseDto> {
    @Mappings
    override fun toModel(r: ExpenseRequest): Expense

    @Mappings
    override fun toDto(m: Expense): ExpenseDto

    @Mappings
    override fun toRequest(d: ExpenseDto): ExpenseRequest

    override fun update(r: ExpenseRequest, @MappingTarget m: Expense)
}
