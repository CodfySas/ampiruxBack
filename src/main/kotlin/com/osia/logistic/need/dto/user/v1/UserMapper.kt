package com.osia.logistic.need.dto.user.v1

import com.osia.logistic.need.model.User
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface UserMapper {

    fun toDto(user: User): UserDto

    fun toModel(userRequest: UserRequest): User

    fun updateModel(userRequest: UserRequest, @MappingTarget user: User)
}
