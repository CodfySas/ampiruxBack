package com.osia.nota_maestro.dto.directorStudent.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.DirectorStudent
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
interface DirectorStudentMapper : BaseMapper<DirectorStudentRequest, DirectorStudent, DirectorStudentDto> {
    @Mappings
    override fun toModel(r: DirectorStudentRequest): DirectorStudent

    @Mappings
    override fun toDto(m: DirectorStudent): DirectorStudentDto

    @Mappings
    override fun toRequest(d: DirectorStudentDto): DirectorStudentRequest

    override fun update(r: DirectorStudentRequest, @MappingTarget m: DirectorStudent)
}
