package com.osia.nota_maestro.dto.classroomStudent.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ClassroomStudent
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
interface ClassroomStudentMapper : BaseMapper<ClassroomStudentRequest, ClassroomStudent, ClassroomStudentDto> {
    @Mappings
    override fun toModel(r: ClassroomStudentRequest): ClassroomStudent

    @Mappings
    override fun toDto(m: ClassroomStudent): ClassroomStudentDto

    @Mappings
    override fun toRequest(d: ClassroomStudentDto): ClassroomStudentRequest

    override fun update(r: ClassroomStudentRequest, @MappingTarget m: ClassroomStudent)
}
