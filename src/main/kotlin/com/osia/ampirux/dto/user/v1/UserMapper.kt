package com.osia.ampirux.dto.user.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.User
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
interface UserMapper : BaseMapper<UserRequest, User, UserDto> {
    @Mappings
    override fun toModel(r: UserRequest): User

    @Mappings
    override fun toDto(m: User): UserDto

    @Mappings
    override fun toRequest(d: UserDto): UserRequest

    override fun update(r: UserRequest, @MappingTarget m: User)
}
