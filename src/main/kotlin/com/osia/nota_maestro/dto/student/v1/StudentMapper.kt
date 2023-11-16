package com.osia.nota_maestro.dto.student.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Student
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
interface StudentMapper : BaseMapper<StudentRequest, Student, StudentDto> {
    @Mappings
    override fun toModel(r: StudentRequest): Student

    @Mappings
    override fun toDto(m: Student): StudentDto

    @Mappings
    override fun toRequest(d: StudentDto): StudentRequest

    override fun update(r: StudentRequest, @MappingTarget m: Student)
}
