package com.osia.nota_maestro.dto.accompanimentStudent.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.AccompanimentStudent
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
interface AccompanimentStudentMapper : BaseMapper<AccompanimentStudentRequest, AccompanimentStudent, AccompanimentStudentDto> {
    @Mappings
    override fun toModel(r: AccompanimentStudentRequest): AccompanimentStudent

    @Mappings
    override fun toDto(m: AccompanimentStudent): AccompanimentStudentDto

    @Mappings
    override fun toRequest(d: AccompanimentStudentDto): AccompanimentStudentRequest

    override fun update(r: AccompanimentStudentRequest, @MappingTarget m: AccompanimentStudent)
}
