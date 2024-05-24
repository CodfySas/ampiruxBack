package com.osia.nota_maestro.dto.postReact.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.PostReact
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
interface PostReactMapper : BaseMapper<PostReactRequest, PostReact, PostReactDto> {
    @Mappings
    override fun toModel(r: PostReactRequest): PostReact

    @Mappings
    override fun toDto(m: PostReact): PostReactDto

    @Mappings
    override fun toRequest(d: PostReactDto): PostReactRequest

    override fun update(r: PostReactRequest, @MappingTarget m: PostReact)
}
