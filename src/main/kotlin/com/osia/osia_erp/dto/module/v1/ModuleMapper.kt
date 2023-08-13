package com.osia.osia_erp.dto.module.v1

import com.osia.osia_erp.dto.BaseMapper
import com.osia.osia_erp.model.Module
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
interface ModuleMapper : BaseMapper<ModuleRequest, Module, ModuleDto> {
    @Mappings
    override fun toModel(r: ModuleRequest): Module

    @Mappings
    override fun toDto(m: Module): ModuleDto

    @Mappings
    override fun toRequest(d: ModuleDto): ModuleRequest

    override fun update(r: ModuleRequest, @MappingTarget m: Module)
}
