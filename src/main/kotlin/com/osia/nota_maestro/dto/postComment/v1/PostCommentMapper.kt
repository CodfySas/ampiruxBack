package com.osia.nota_maestro.dto.postComment.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.PostComment
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
interface PostCommentMapper : BaseMapper<PostCommentRequest, PostComment, PostCommentDto> {
    @Mappings
    override fun toModel(r: PostCommentRequest): PostComment

    @Mappings
    override fun toDto(m: PostComment): PostCommentDto

    @Mappings
    override fun toRequest(d: PostCommentDto): PostCommentRequest

    override fun update(r: PostCommentRequest, @MappingTarget m: PostComment)
}
