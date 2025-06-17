package com.osia.ampirux.dto.notification.v1

import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.model.enums.NotificationEnum
import java.time.LocalDateTime
import java.util.UUID

data class NotificationMessageDto(
    var userUuid: UUID,
    var senderUuid: UUID?,
    var type: NotificationEnum,
    var message: String = "",
    var read: Boolean = false,
    var createdAt: LocalDateTime,
    var link: String,
    var userSender: UserDto? = null,
    var postUuid: UUID? = null
)
