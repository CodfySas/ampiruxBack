package com.osia.nota_maestro.dto.subModuleUser.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.SubModuleUser
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
interface SubModuleUserMapper :
    com.osia.nota_maestro.dto.BaseMapper<SubModuleUserRequest, SubModuleUser, SubModuleUserDto> {
    @Mappings
    override fun toModel(r: SubModuleUserRequest): SubModuleUser

    @Mappings
    override fun toDto(m: SubModuleUser): SubModuleUserDto

    @Mappings
    override fun toRequest(d: SubModuleUserDto): SubModuleUserRequest

    override fun update(r: SubModuleUserRequest, @MappingTarget m: SubModuleUser)
}
