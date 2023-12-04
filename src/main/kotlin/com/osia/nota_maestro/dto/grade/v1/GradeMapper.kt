package com.osia.nota_maestro.dto.grade.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Grade
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
interface GradeMapper : BaseMapper<GradeRequest, Grade, GradeDto> {
    @Mappings
    override fun toModel(r: GradeRequest): Grade

    @Mappings
    override fun toDto(m: Grade): GradeDto

    @Mappings
    fun toComplete(m: Grade): GradeCompleteDto

    @Mappings
    override fun toRequest(d: GradeDto): GradeRequest

    override fun update(r: GradeRequest, @MappingTarget m: Grade)
}
