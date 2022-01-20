package com.osia.logistic.need.dto

import java.time.LocalDateTime
import java.util.UUID

open class BaseDto {
    var uuid: UUID? = null
    var code: String? = null
    var createdAt: LocalDateTime? = null
    var lastModifiedAt: LocalDateTime? = null
}
