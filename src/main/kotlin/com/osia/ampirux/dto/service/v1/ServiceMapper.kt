package com.osia.ampirux.dto.service.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Service
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
interface ServiceMapper : BaseMapper<ServiceRequest, Service, ServiceDto> {
    @Mappings
    override fun toModel(r: ServiceRequest): Service

    @Mappings
    override fun toDto(m: Service): ServiceDto

    @Mappings
    override fun toRequest(d: ServiceDto): ServiceRequest

    override fun update(r: ServiceRequest, @MappingTarget m: Service)
}
