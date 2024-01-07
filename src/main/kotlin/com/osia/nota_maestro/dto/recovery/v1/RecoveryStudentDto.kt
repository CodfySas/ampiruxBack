package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto

class RecoveryStudentDto : BaseDto() {
    var name: String? = null
    var lastname: String? = null
    var subjects: List<RecoverySubjectsDto>? = null
}
