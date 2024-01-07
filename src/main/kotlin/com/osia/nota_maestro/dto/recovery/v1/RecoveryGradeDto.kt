package com.osia.nota_maestro.dto.recovery.v1

import com.osia.nota_maestro.dto.BaseDto

class RecoveryGradeDto : BaseDto() {
    var name: String? = null
    var classrooms: List<RecoveryClassroomDto>? = null
}
