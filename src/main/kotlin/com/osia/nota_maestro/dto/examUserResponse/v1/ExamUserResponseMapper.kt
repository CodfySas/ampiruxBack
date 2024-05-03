package com.osia.nota_maestro.dto.examUserResponse.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ExamUserResponse
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
interface ExamUserResponseMapper : BaseMapper<ExamUserResponseRequest, ExamUserResponse, ExamUserResponseDto> {
    @Mappings
    override fun toModel(r: ExamUserResponseRequest): ExamUserResponse

    @Mappings
    override fun toDto(m: ExamUserResponse): ExamUserResponseDto

    @Mappings
    override fun toRequest(d: ExamUserResponseDto): ExamUserResponseRequest

    override fun update(r: ExamUserResponseRequest, @MappingTarget m: ExamUserResponse)
}
