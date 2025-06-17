package com.osia.ampirux.dto.conversation.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.message.v1.MessageDto
import com.osia.ampirux.dto.user.v1.UserMinDto
import org.springframework.data.domain.Page
import java.util.UUID

class ConversationDto : BaseDto() {
    var userOneUuid: UUID? = null
    var userTwoUuid: UUID? = null
    var messages: Page<MessageDto>? = null
    var userOne: UserMinDto? = null
    var userTwo: UserMinDto? = null
}
