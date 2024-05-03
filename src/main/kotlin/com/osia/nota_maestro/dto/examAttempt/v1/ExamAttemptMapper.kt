package com.osia.nota_maestro.dto.examAttempt.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ExamAttempt
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
interface ExamAttemptMapper : BaseMapper<ExamAttemptRequest, ExamAttempt, ExamAttemptDto> {
    @Mappings
    override fun toModel(r: ExamAttemptRequest): ExamAttempt

    @Mappings
    override fun toDto(m: ExamAttempt): ExamAttemptDto

    @Mappings
    override fun toRequest(d: ExamAttemptDto): ExamAttemptRequest

    override fun update(r: ExamAttemptRequest, @MappingTarget m: ExamAttempt)
}
