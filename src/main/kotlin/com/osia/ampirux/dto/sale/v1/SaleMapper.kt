package com.osia.ampirux.dto.sale.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Sale
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
interface SaleMapper : BaseMapper<SaleRequest, Sale, SaleDto> {
    @Mappings
    override fun toModel(r: SaleRequest): Sale

    @Mappings
    override fun toDto(m: Sale): SaleDto

    @Mappings
    override fun toRequest(d: SaleDto): SaleRequest

    override fun update(r: SaleRequest, @MappingTarget m: Sale)
}
