package com.osia.nota_maestro.dto.diagnosis.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Diagnosis
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
interface DiagnosisMapper : BaseMapper<DiagnosisRequest, Diagnosis, DiagnosisDto> {
    @Mappings
    override fun toModel(r: DiagnosisRequest): Diagnosis

    @Mappings
    override fun toDto(m: Diagnosis): DiagnosisDto

    @Mappings
    override fun toRequest(d: DiagnosisDto): DiagnosisRequest

    override fun update(r: DiagnosisRequest, @MappingTarget m: Diagnosis)
}
