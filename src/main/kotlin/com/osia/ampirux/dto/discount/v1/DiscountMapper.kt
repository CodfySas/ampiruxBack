package com.osia.ampirux.dto.discount.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Discount
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
interface DiscountMapper : BaseMapper<DiscountRequest, Discount, DiscountDto> {
    @Mappings
    override fun toModel(r: DiscountRequest): Discount

    @Mappings
    override fun toDto(m: Discount): DiscountDto

    @Mappings
    override fun toRequest(d: DiscountDto): DiscountRequest

    override fun update(r: DiscountRequest, @MappingTarget m: Discount)
}
