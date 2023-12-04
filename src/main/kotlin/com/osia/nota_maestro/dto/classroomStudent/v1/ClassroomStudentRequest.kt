package com.osia.nota_maestro.dto.classroomStudent.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class ClassroomStudentRequest {
    var uuidStudent: UUID? = null
    var uuidClassroom: UUID? = null
}
