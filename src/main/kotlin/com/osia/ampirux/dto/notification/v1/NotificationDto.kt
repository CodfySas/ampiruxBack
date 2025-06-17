package com.osia.ampirux.dto.notification.v1

import com.osia.ampirux.dto.BaseDto
import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.model.enums.NotificationEnum
import java.util.UUID

class NotificationDto : BaseDto() {
    var userUuid: UUID? = null
    var senderUuid: UUID? = null
    var type: NotificationEnum? = null
    var message: String = ""
    var read: Boolean = false
    var link: String = ""
    var userSender: UserDto? = null
    var postUuid: UUID? = null
}
