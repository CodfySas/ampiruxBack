package com.osia.nota_maestro.dto.examQuestion.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ExamQuestion
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
interface ExamQuestionMapper : BaseMapper<ExamQuestionRequest, ExamQuestion, ExamQuestionDto> {
    @Mappings
    override fun toModel(r: ExamQuestionRequest): ExamQuestion

    @Mappings
    override fun toDto(m: ExamQuestion): ExamQuestionDto

    @Mappings
    override fun toRequest(d: ExamQuestionDto): ExamQuestionRequest

    override fun update(r: ExamQuestionRequest, @MappingTarget m: ExamQuestion)
}
