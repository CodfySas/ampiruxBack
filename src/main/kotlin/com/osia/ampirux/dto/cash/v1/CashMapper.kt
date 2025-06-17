package com.osia.ampirux.dto.cash.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Cash
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
interface CashMapper : BaseMapper<CashRequest, Cash, CashDto> {
    @Mappings
    override fun toModel(r: CashRequest): Cash

    @Mappings
    override fun toDto(m: Cash): CashDto

    @Mappings
    override fun toRequest(d: CashDto): CashRequest

    override fun update(r: CashRequest, @MappingTarget m: Cash)
}
