package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto

class RecoveryClassroomDto : BaseDto() {
    var name: String? = null
    var students: List<RecoveryStudentDto>? = null
}
