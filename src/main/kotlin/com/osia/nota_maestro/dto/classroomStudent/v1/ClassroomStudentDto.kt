package com.osia.nota_maestro.dto.classroomStudent.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ClassroomStudentDto : BaseDto() {
    var uuidStudent: UUID? = null
    var uuidClassroom: UUID? = null
    var prom: Double? = null
}
