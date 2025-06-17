package com.osia.ampirux.repository.message

import com.osia.ampirux.model.Message
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("message.crud_repository")
interface MessageRepository : CommonRepository<Message> {
    fun findAllByReceiverUuidInOrSenderUuidIn(userUuids: List<UUID>, userTwos: List<UUID>, pageable: Pageable): Page<Message>
    fun findAllByConversationUuid(conversationUuid: UUID, pageable: Pageable): Page<Message>
    fun getAllByConversationUuidAndReceiverUuid(conversationUuid: UUID, receiverUuid: UUID): List<Message>
}
