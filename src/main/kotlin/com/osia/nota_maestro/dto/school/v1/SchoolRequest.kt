package com.osia.nota_maestro.dto.school.v1

import java.time.LocalDateTime

class SchoolRequest {
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
