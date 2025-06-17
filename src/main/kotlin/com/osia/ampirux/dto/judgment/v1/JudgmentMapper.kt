package com.osia.ampirux.dto.judgment.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Judgment
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
interface JudgmentMapper : BaseMapper<JudgmentRequest, Judgment, JudgmentDto> {
    @Mappings
    override fun toModel(r: JudgmentRequest): Judgment

    @Mappings
    override fun toDto(m: Judgment): JudgmentDto

    @Mappings
    override fun toRequest(d: JudgmentDto): JudgmentRequest

    override fun update(r: JudgmentRequest, @MappingTarget m: Judgment)
}
