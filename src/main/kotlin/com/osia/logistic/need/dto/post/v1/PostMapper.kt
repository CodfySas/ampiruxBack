package com.osia.logistic.need.dto.post.v1

import com.osia.logistic.need.model.Post
import org.mapstruct.Mapper
import org.mapstruct.Mapping
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
interface PostMapper {
    @Mappings(
        value =
        [
            Mapping(target = "clientDto.name", source = "post.client.name"),
        ]
    )
    fun toDto(post: Post): PostDto

    fun toModel(postRequest: PostRequest): Post

    fun updateModel(postRequest: PostRequest, @MappingTarget post: Post)
}
