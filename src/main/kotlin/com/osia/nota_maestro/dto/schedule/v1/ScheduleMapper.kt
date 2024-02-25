package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Schedule
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
interface ScheduleMapper : BaseMapper<ScheduleRequest, Schedule, ScheduleDto> {
    @Mappings
    override fun toModel(r: ScheduleRequest): Schedule

    @Mappings
    override fun toDto(m: Schedule): ScheduleDto

    @Mappings
    override fun toRequest(d: ScheduleDto): ScheduleRequest

    override fun update(r: ScheduleRequest, @MappingTarget m: Schedule)
}
