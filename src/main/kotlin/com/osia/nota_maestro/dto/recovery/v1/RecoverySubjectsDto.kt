package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class RecoverySubjectsDto : BaseDto() {
    var name: String? = null
    var periods: List<RecoveryPeriodDto>? = mutableListOf()
    var def: Double? = null
    var recovery: Double? = null
    var uuidStudentSubject: UUID? = null
    var enabled: Boolean = false
}
