package com.osia.nota_maestro.dto.studentSubject.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.StudentSubject
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
interface StudentSubjectMapper : BaseMapper<StudentSubjectRequest, StudentSubject, StudentSubjectDto> {
    @Mappings
    override fun toModel(r: StudentSubjectRequest): StudentSubject

    @Mappings
    override fun toDto(m: StudentSubject): StudentSubjectDto

    @Mappings
    override fun toRequest(d: StudentSubjectDto): StudentSubjectRequest

    override fun update(r: StudentSubjectRequest, @MappingTarget m: StudentSubject)
}
