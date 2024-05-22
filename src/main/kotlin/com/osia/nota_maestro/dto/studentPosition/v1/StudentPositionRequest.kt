package com.osia.nota_maestro.dto.studentPosition.v1

import java.util.UUID

class StudentPositionRequest {
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var position: Int? = null
    var prom: Double? = null
}
