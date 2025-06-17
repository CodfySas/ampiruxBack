package com.osia.ampirux.dto.saleservice.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.SaleService
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
interface SaleServiceMapper : BaseMapper<SaleServiceRequest, SaleService, SaleServiceDto> {
    @Mappings
    override fun toModel(r: SaleServiceRequest): SaleService

    @Mappings
    override fun toDto(m: SaleService): SaleServiceDto

    @Mappings
    override fun toRequest(d: SaleServiceDto): SaleServiceRequest

    override fun update(r: SaleServiceRequest, @MappingTarget m: SaleService)
}
