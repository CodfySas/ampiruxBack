package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ClassroomSubject
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
interface ClassroomSubjectMapper : BaseMapper<ClassroomSubjectRequest, ClassroomSubject, ClassroomSubjectDto> {
    @Mappings
    override fun toModel(r: ClassroomSubjectRequest): ClassroomSubject

    @Mappings
    override fun toDto(m: ClassroomSubject): ClassroomSubjectDto

    @Mappings
    override fun toRequest(d: ClassroomSubjectDto): ClassroomSubjectRequest

    override fun update(r: ClassroomSubjectRequest, @MappingTarget m: ClassroomSubject)
}
