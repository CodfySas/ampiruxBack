package com.osia.nota_maestro.dto.accompaniment.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Accompaniment
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
interface AccompanimentMapper : BaseMapper<AccompanimentRequest, Accompaniment, AccompanimentDto> {
    @Mappings
    override fun toModel(r: AccompanimentRequest): Accompaniment

    @Mappings
    override fun toDto(m: Accompaniment): AccompanimentDto

    @Mappings
    override fun toRequest(d: AccompanimentDto): AccompanimentRequest

    override fun update(r: AccompanimentRequest, @MappingTarget m: Accompaniment)
}
