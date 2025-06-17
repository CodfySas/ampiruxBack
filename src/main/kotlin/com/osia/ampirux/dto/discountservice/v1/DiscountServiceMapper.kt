package com.osia.ampirux.dto.discountservice.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.DiscountService
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
interface DiscountServiceMapper : BaseMapper<DiscountServiceRequest, DiscountService, DiscountServiceDto> {
    @Mappings
    override fun toModel(r: DiscountServiceRequest): DiscountService

    @Mappings
    override fun toDto(m: DiscountService): DiscountServiceDto

    @Mappings
    override fun toRequest(d: DiscountServiceDto): DiscountServiceRequest

    override fun update(r: DiscountServiceRequest, @MappingTarget m: DiscountService)
}
