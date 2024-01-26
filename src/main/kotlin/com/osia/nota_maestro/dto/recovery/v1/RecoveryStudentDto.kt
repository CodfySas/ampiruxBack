package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class RecoveryStudentDto : BaseDto() {
    var name: String? = null
    var lastname: String? = null
    var periods: List<RecoveryPeriodDto>? = mutableListOf()
    var def: String? = null
    var recovery: String? = null
    var uuidStudentSubject: UUID? = null
    var enabled: Boolean = false
}
