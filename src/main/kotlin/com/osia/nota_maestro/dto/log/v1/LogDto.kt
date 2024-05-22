package com.osia.nota_maestro.dto.log.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDate
import java.util.UUID

class LogDto : BaseDto() {
    var uuidUser: UUID? = null
    var userName: String? = null
    var userCode: String? = null
    var userRole: String? = null
    var movement: String? = null
    var day: LocalDate? = null
    var hour: String? = null
    var status: String? = null
    var detail: String? = null
}
