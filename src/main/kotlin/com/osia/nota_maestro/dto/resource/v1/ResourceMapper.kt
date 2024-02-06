package com.osia.nota_maestro.dto.resource.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Resource
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
interface ResourceMapper : BaseMapper<ResourceRequest, Resource, ResourceDto> {
    @Mappings
    override fun toModel(r: ResourceRequest): Resource

    @Mappings
    override fun toDto(m: Resource): ResourceDto

    @Mappings
    override fun toRequest(d: ResourceDto): ResourceRequest

    override fun update(r: ResourceRequest, @MappingTarget m: Resource)
}
