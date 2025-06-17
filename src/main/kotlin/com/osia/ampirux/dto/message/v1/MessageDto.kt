package com.osia.ampirux.dto.message.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.user.v1.UserDto
import java.util.UUID

class MessageDto : BaseDto() {
    var message: String? = null
    var senderUuid: UUID? = null
    var receiverUuid: UUID? = null
    var sender: UserDto? = null
    var conversationUuid: UUID? = null
    var read: Boolean? = null
}
