package com.osia.nota_maestro.dto.attendanceFail.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.AttendanceFail
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
interface AttendanceFailMapper : BaseMapper<AttendanceFailRequest, AttendanceFail, AttendanceFailDto> {
    @Mappings
    override fun toModel(r: AttendanceFailRequest): AttendanceFail

    @Mappings
    override fun toDto(m: AttendanceFail): AttendanceFailDto

    @Mappings
    override fun toRequest(d: AttendanceFailDto): AttendanceFailRequest

    override fun update(r: AttendanceFailRequest, @MappingTarget m: AttendanceFail)
}
