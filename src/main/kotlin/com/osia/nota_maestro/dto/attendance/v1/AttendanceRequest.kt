package com.osia.nota_maestro.dto.attendance.v1

import java.util.UUID

class AttendanceRequest {
    var uuidSchool: UUID? = null
    var uuidClassroom: UUID? = null
    var day: Int? = null
    var month: Int? = null
    var uuidSubject: UUID? = null
}
