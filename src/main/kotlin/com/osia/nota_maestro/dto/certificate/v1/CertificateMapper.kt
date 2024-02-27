package com.osia.nota_maestro.dto.certificate.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Certificate
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
interface CertificateMapper : BaseMapper<CertificateRequest, Certificate, CertificateDto> {
    @Mappings
    override fun toModel(r: CertificateRequest): Certificate

    @Mappings
    override fun toDto(m: Certificate): CertificateDto

    @Mappings
    override fun toRequest(d: CertificateDto): CertificateRequest

    override fun update(r: CertificateRequest, @MappingTarget m: Certificate)
}
