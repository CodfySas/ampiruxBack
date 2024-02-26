package com.osia.nota_maestro.dto.attendanceFail.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AttendanceFailDto : BaseDto() {
    var reason: String? = null
    var uuidAttendance: UUID? = null
    var uuidStudent: UUID? = null
}
