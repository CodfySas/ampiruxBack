package com.osia.nota_maestro.dto.teacher.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Teacher
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
interface TeacherMapper : BaseMapper<TeacherRequest, Teacher, TeacherDto> {
    @Mappings
    override fun toModel(r: TeacherRequest): Teacher

    @Mappings
    override fun toDto(m: Teacher): TeacherDto

    @Mappings
    override fun toRequest(d: TeacherDto): TeacherRequest

    override fun update(r: TeacherRequest, @MappingTarget m: Teacher)
}
