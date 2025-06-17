package com.osia.ampirux.dto.saleserviceproduct.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.SaleServiceProduct
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
interface SaleServiceProductMapper : BaseMapper<SaleServiceProductRequest, SaleServiceProduct, SaleServiceProductDto> {
    @Mappings
    override fun toModel(r: SaleServiceProductRequest): SaleServiceProduct

    @Mappings
    override fun toDto(m: SaleServiceProduct): SaleServiceProductDto

    @Mappings
    override fun toRequest(d: SaleServiceProductDto): SaleServiceProductRequest

    override fun update(r: SaleServiceProductRequest, @MappingTarget m: SaleServiceProduct)
}
