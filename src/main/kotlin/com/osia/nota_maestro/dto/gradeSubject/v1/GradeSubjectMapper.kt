package com.osia.nota_maestro.dto.gradeSubject.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.GradeSubject
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
interface GradeSubjectMapper : BaseMapper<GradeSubjectRequest, GradeSubject, GradeSubjectDto> {
    @Mappings
    override fun toModel(r: GradeSubjectRequest): GradeSubject

    @Mappings
    override fun toDto(m: GradeSubject): GradeSubjectDto

    @Mappings
    override fun toRequest(d: GradeSubjectDto): GradeSubjectRequest

    override fun update(r: GradeSubjectRequest, @MappingTarget m: GradeSubject)
}
