package com.osia.nota_maestro.dto.attendanceFail.v1

import java.util.UUID

class AttendanceFailRequest {
    var reason: String? = null
    var uuidAttendance: UUID? = null
    var uuidStudent: UUID? = null
}
