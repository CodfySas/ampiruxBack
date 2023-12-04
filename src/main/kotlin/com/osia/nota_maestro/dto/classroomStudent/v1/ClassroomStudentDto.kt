package com.osia.nota_maestro.dto.classroomStudent.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class ClassroomStudentDto : BaseDto() {
    var uuidStudent: UUID? = null
    var uuidClassroom: UUID? = null
}
