package com.osia.nota_maestro.dto.log.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Log
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
interface LogMapper : BaseMapper<LogRequest, Log, LogDto> {
    @Mappings
    override fun toModel(r: LogRequest): Log

    @Mappings
    override fun toDto(m: Log): LogDto

    @Mappings
    override fun toRequest(d: LogDto): LogRequest

    override fun update(r: LogRequest, @MappingTarget m: Log)
}
