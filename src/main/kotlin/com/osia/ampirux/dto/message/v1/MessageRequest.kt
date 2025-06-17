package com.osia.ampirux.dto.message.v1

import java.util.UUID

class MessageRequest {
    var message: String? = null
    var senderUuid: UUID? = null
    var receiverUuid: UUID? = null
    var conversationUuid: UUID? = null
    var read: Boolean? = null
}
