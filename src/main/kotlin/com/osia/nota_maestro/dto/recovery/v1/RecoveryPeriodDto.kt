package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class RecoveryPeriodDto : BaseDto() {
    var number: Int? = null
    var def: Double? = null
    var uuidStudentSubject: UUID? = null
    var recovery: Double? = null
    var enabled: Boolean = false
}
