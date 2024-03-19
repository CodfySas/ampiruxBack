package com.osia.nota_maestro.dto.examResponse.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ExamResponse
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
interface ExamResponseMapper : BaseMapper<ExamResponseRequest, ExamResponse, ExamResponseDto> {
    @Mappings
    override fun toModel(r: ExamResponseRequest): ExamResponse

    @Mappings
    override fun toDto(m: ExamResponse): ExamResponseDto

    @Mappings
    override fun toRequest(d: ExamResponseDto): ExamResponseRequest

    override fun update(r: ExamResponseRequest, @MappingTarget m: ExamResponse)
}
