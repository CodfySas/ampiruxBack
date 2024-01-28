package com.osia.nota_maestro.dto.school.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.School
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
interface SchoolMapper : BaseMapper<SchoolRequest, School, SchoolDto> {
    @Mappings
    override fun toModel(r: SchoolRequest): School

    @Mappings
    override fun toDto(m: School): SchoolDto

    @Mappings
    fun toMin(m: School): SchoolMinDto

    @Mappings
    override fun toRequest(d: SchoolDto): SchoolRequest

    override fun update(r: SchoolRequest, @MappingTarget m: School)
}
