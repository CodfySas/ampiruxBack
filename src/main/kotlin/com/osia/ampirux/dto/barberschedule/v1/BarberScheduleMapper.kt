package com.osia.ampirux.dto.barberschedule.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.BarberSchedule
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
interface BarberScheduleMapper : BaseMapper<BarberScheduleRequest, BarberSchedule, BarberScheduleDto> {
    @Mappings
    override fun toModel(r: BarberScheduleRequest): BarberSchedule

    @Mappings
    override fun toDto(m: BarberSchedule): BarberScheduleDto

    @Mappings
    override fun toRequest(d: BarberScheduleDto): BarberScheduleRequest

    override fun update(r: BarberScheduleRequest, @MappingTarget m: BarberSchedule)
}
