package com.osia.osia_erp.dto.calendar.v1

import com.osia.osia_erp.dto.BaseMapper
import com.osia.osia_erp.model.CalendarTask
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
