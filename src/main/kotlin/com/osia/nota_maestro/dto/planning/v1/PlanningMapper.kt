package com.osia.nota_maestro.dto.planning.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Planning
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
interface PlanningMapper : BaseMapper<PlanningRequest, Planning, PlanningDto> {
    @Mappings
    override fun toModel(r: PlanningRequest): Planning

    @Mappings
    override fun toDto(m: Planning): PlanningDto

    @Mappings
    override fun toRequest(d: PlanningDto): PlanningRequest

    override fun update(r: PlanningRequest, @MappingTarget m: Planning)
}
