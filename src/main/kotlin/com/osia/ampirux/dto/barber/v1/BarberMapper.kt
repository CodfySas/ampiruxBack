package com.osia.ampirux.dto.barber.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Barber
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
interface BarberMapper : BaseMapper<BarberRequest, Barber, BarberDto> {
    @Mappings
    override fun toModel(r: BarberRequest): Barber

    @Mappings
    override fun toDto(m: Barber): BarberDto

    @Mappings
    override fun toRequest(d: BarberDto): BarberRequest

    override fun update(r: BarberRequest, @MappingTarget m: Barber)
}
