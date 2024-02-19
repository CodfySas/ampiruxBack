package com.osia.nota_maestro.dto.director.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Director
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
interface DirectorMapper : BaseMapper<DirectorRequest, Director, DirectorDto> {
    @Mappings
    override fun toModel(r: DirectorRequest): Director

    @Mappings
    override fun toDto(m: Director): DirectorDto

    @Mappings
    override fun toRequest(d: DirectorDto): DirectorRequest

    override fun update(r: DirectorRequest, @MappingTarget m: Director)
}
