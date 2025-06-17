package com.osia.ampirux.dto.conversation.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Conversation
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
interface ConversationMapper : BaseMapper<ConversationRequest, Conversation, ConversationDto> {
    @Mappings
    override fun toModel(r: ConversationRequest): Conversation

    @Mappings
    override fun toDto(m: Conversation): ConversationDto

    @Mappings
    override fun toRequest(d: ConversationDto): ConversationRequest

    override fun update(r: ConversationRequest, @MappingTarget m: Conversation)
}
