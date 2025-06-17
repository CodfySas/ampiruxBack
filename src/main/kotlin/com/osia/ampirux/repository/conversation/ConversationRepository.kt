package com.osia.ampirux.repository.conversation

import com.osia.ampirux.model.Conversation
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("conversation.crud_repository")
interface ConversationRepository : CommonRepository<Conversation> {
    fun findAllByUserOneUuidOrUserTwoUuid(userUuid: UUID, userTwo: UUID, pageable: Pageable): Page<Conversation>
    fun findFirstByUserOneUuidAndUserTwoUuidOrUserOneUuidAndUserTwoUuid(
        userOne: UUID,
        userTwo: UUID,
        userTwoAlt: UUID,
        userOneAlt: UUID
    ): Optional<Conversation>
}
