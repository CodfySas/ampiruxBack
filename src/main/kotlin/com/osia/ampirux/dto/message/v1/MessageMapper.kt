package com.osia.ampirux.dto.message.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Message
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
interface MessageMapper : BaseMapper<MessageRequest, Message, MessageDto> {
    @Mappings
    override fun toModel(r: MessageRequest): Message

    @Mappings
    override fun toDto(m: Message): MessageDto

    @Mappings
    override fun toRequest(d: MessageDto): MessageRequest

    override fun update(r: MessageRequest, @MappingTarget m: Message)
}
