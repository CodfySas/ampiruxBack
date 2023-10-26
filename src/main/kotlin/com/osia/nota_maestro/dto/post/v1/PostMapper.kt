package com.osia.nota_maestro.dto.post.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Post
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface PostMapper : BaseMapper<PostRequest, Post, PostDto> {
    @Mappings
    override fun toModel(r: PostRequest): Post

    @Mappings
    override fun toDto(m: Post): PostDto

    @Mappings
    override fun toRequest(d: PostDto): PostRequest

    override fun update(r: PostRequest, @MappingTarget m: Post)
}
