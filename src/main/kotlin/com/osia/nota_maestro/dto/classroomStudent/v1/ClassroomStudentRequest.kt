package com.osia.nota_maestro.dto.classroomStudent.v1

import java.util.UUID

class ClassroomStudentRequest {
    var uuidStudent: UUID? = null
    var uuidClassroom: UUID? = null
    var prom: Double? = null
}
