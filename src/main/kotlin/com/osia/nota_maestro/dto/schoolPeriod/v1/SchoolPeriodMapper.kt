package com.osia.nota_maestro.dto.schoolPeriod.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.SchoolPeriod
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
interface SchoolPeriodMapper : BaseMapper<SchoolPeriodRequest, SchoolPeriod, SchoolPeriodDto> {
    @Mappings
    override fun toModel(r: SchoolPeriodRequest): SchoolPeriod

    @Mappings
    override fun toDto(m: SchoolPeriod): SchoolPeriodDto

    @Mappings
    override fun toRequest(d: SchoolPeriodDto): SchoolPeriodRequest

    override fun update(r: SchoolPeriodRequest, @MappingTarget m: SchoolPeriod)
}
