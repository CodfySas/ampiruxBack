package com.osia.ampirux.dto.saleproduct.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.SaleProduct
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
interface SaleProductMapper : BaseMapper<SaleProductRequest, SaleProduct, SaleProductDto> {
    @Mappings
    override fun toModel(r: SaleProductRequest): SaleProduct

    @Mappings
    override fun toDto(m: SaleProduct): SaleProductDto

    @Mappings
    override fun toRequest(d: SaleProductDto): SaleProductRequest

    override fun update(r: SaleProductRequest, @MappingTarget m: SaleProduct)
}
