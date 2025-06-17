package com.osia.ampirux.dto.barbershop.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.BarberShop
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
interface BarberShopMapper : BaseMapper<BarberShopRequest, BarberShop, BarberShopDto> {
    @Mappings
    override fun toModel(r: BarberShopRequest): BarberShop

    @Mappings
    override fun toDto(m: BarberShop): BarberShopDto

    @Mappings
    override fun toRequest(d: BarberShopDto): BarberShopRequest

    override fun update(r: BarberShopRequest, @MappingTarget m: BarberShop)
}

