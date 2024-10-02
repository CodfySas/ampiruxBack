package com.osia.nota_maestro.dto.preliminary.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Preliminary
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
interface PreliminaryMapper : BaseMapper<PreliminaryRequest, Preliminary, PreliminaryDto> {
    @Mappings
    override fun toModel(r: PreliminaryRequest): Preliminary

    @Mappings
    override fun toDto(m: Preliminary): PreliminaryDto

    @Mappings
    override fun toRequest(d: PreliminaryDto): PreliminaryRequest

    override fun update(r: PreliminaryRequest, @MappingTarget m: Preliminary)
}
