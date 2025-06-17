package com.osia.ampirux.service.conversation

import com.osia.ampirux.dto.conversation.v1.ConversationDto
import com.osia.ampirux.dto.conversation.v1.ConversationRequest
import com.osia.ampirux.model.Conversation
import com.osia.ampirux.service.common.CommonService
import org.springframework.data.domain.Page
import java.util.UUID

interface ConversationService : CommonService<Conversation, ConversationDto, ConversationRequest> {
    fun getAllByUser(user: UUID, page: Int, size: Int): Page<ConversationDto>
    fun getUserConversation(user: UUID, to: String, page: Int, size: Int): ConversationDto
    fun sendMessage(user: UUID, to: UUID, message: String): Boolean
    fun readMessages(user: UUID, to: UUID): Boolean
}
