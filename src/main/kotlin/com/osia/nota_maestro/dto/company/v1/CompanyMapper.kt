package com.osia.nota_maestro.dto.company.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Company
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
interface CompanyMapper : BaseMapper<CompanyRequest, Company, CompanyDto> {
    @Mappings
    override fun toModel(r: CompanyRequest): Company

    @Mappings
    override fun toDto(m: Company): CompanyDto

    @Mappings
    override fun toRequest(d: CompanyDto): CompanyRequest

    override fun update(r: CompanyRequest, @MappingTarget m: Company)
}
