package com.osia.ampirux.dto.appointment.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Appointment
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
interface AppointmentMapper : BaseMapper<AppointmentRequest, Appointment, AppointmentDto> {
    @Mappings
    override fun toModel(r: AppointmentRequest): Appointment

    @Mappings
    override fun toDto(m: Appointment): AppointmentDto

    @Mappings
    override fun toRequest(d: AppointmentDto): AppointmentRequest

    override fun update(r: AppointmentRequest, @MappingTarget m: Appointment)
}
