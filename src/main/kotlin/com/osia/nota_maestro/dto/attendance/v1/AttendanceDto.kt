package com.osia.nota_maestro.dto.attendance.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AttendanceDto : BaseDto() {
    var uuidSchool: UUID? = null
    var uuidClassroom: UUID? = null
    var uuidSubject: UUID? = null
    var day: Int? = null
    var week: String? = null
    var month: Int? = null
    var enabled: Boolean? = null

    var failed: Boolean? = false
    var reason: String? = null
    var failUuid: UUID? = null
}
