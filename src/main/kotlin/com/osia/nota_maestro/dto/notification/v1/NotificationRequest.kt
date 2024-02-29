package com.osia.nota_maestro.dto.notification.v1

import java.time.LocalDateTime
import java.util.UUID

class NotificationRequest {
    var uuidUser: UUID? = null
    var description: String? = null
    var urlLink: String? = null
    var uuidSchool: UUID? = null
    var viewed: Boolean? = false
    var datetime: LocalDateTime? = null
    var type: String? = null
}
