package com.osia.osia_erp.dto.subModule.v1

import com.osia.osia_erp.dto.BaseMapper
import com.osia.osia_erp.model.SubModule
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
interface SubModuleMapper : BaseMapper<SubModuleRequest, SubModule, SubModuleDto> {
    @Mappings
    override fun toModel(r: SubModuleRequest): SubModule

    @Mappings
    override fun toDto(m: SubModule): SubModuleDto

    @Mappings
    override fun toRequest(d: SubModuleDto): SubModuleRequest

    override fun update(r: SubModuleRequest, @MappingTarget m: SubModule)
}
