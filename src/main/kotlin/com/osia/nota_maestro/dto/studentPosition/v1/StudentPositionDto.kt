package com.osia.nota_maestro.dto.studentPosition.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class StudentPositionDto : BaseDto() {
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var position: Int? = null
    var prom: Double? = null
}
