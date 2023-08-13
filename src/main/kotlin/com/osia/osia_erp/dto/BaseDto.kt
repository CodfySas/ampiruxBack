package com.osia.osia_erp.dto

import java.time.LocalDateTime
import java.util.UUID

open class BaseDto {
    var uuid: UUID? = null
    var code: String? = null
    var createdAt: LocalDateTime? = null
    var lastModifiedAt: LocalDateTime? = null
    var deletedAt: LocalDateTime? = null
    var isDeleted: Boolean? = null
}
