package com.osia.nota_maestro.dto.attendance.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Attendance
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
interface AttendanceMapper : BaseMapper<AttendanceRequest, Attendance, AttendanceDto> {
    @Mappings
    override fun toModel(r: AttendanceRequest): Attendance

    @Mappings
    override fun toDto(m: Attendance): AttendanceDto

    @Mappings
    override fun toRequest(d: AttendanceDto): AttendanceRequest

    override fun update(r: AttendanceRequest, @MappingTarget m: Attendance)
}
