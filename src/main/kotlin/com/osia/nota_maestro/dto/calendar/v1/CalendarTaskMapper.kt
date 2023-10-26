package com.osia.nota_maestro.dto.calendar.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.CalendarTask
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
interface CalendarTaskMapper : BaseMapper<CalendarTaskRequest, CalendarTask, CalendarTaskDto> {
    @Mappings
    override fun toModel(r: CalendarTaskRequest): CalendarTask

    @Mappings
    override fun toDto(m: CalendarTask): CalendarTaskDto

    @Mappings
    override fun toRequest(d: CalendarTaskDto): CalendarTaskRequest

    override fun update(r: CalendarTaskRequest, @MappingTarget m: CalendarTask)
}
