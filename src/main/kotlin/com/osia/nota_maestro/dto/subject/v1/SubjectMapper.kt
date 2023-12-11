package com.osia.nota_maestro.dto.subject.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Subject
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
interface SubjectMapper : BaseMapper<SubjectRequest, Subject, SubjectDto> {
    @Mappings
    override fun toModel(r: SubjectRequest): Subject

    @Mappings
    override fun toDto(m: Subject): SubjectDto

    @Mappings
    override fun toRequest(d: SubjectDto): SubjectRequest

    override fun update(r: SubjectRequest, @MappingTarget m: Subject)
}
