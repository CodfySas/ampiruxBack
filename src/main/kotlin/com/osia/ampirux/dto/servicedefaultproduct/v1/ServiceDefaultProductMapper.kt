package com.osia.ampirux.dto.servicedefaultproduct.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.ServiceDefaultProduct
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
interface ServiceDefaultProductMapper : BaseMapper<ServiceDefaultProductRequest, ServiceDefaultProduct, ServiceDefaultProductDto> {
    @Mappings
    override fun toModel(r: ServiceDefaultProductRequest): ServiceDefaultProduct

    @Mappings
    override fun toDto(m: ServiceDefaultProduct): ServiceDefaultProductDto

    @Mappings
    override fun toRequest(d: ServiceDefaultProductDto): ServiceDefaultProductRequest

    override fun update(r: ServiceDefaultProductRequest, @MappingTarget m: ServiceDefaultProduct)
}
