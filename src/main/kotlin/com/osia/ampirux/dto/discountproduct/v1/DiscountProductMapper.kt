package com.osia.ampirux.dto.discountproduct.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.DiscountProduct
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
interface DiscountProductMapper : BaseMapper<DiscountProductRequest, DiscountProduct, DiscountProductDto> {
    @Mappings
    override fun toModel(r: DiscountProductRequest): DiscountProduct

    @Mappings
    override fun toDto(m: DiscountProduct): DiscountProductDto

    @Mappings
    override fun toRequest(d: DiscountProductDto): DiscountProductRequest

    override fun update(r: DiscountProductRequest, @MappingTarget m: DiscountProduct)
}
