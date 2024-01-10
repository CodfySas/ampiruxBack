package com.osia.nota_maestro.dto.notification.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDateTime
import java.util.UUID

class NotificationDto : BaseDto() {
    var uuidUser: UUID? = null
    var description: String? = null
    var urlLink: String? = null
    var uuidSchool: String? = null
    var viewed: Boolean? = false
    var datetime: LocalDateTime? = null
    var type: String? = null
}
