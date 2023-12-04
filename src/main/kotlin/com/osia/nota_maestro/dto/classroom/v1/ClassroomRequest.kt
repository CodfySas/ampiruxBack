package com.osia.nota_maestro.dto.classroom.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class ClassroomRequest {
    var name: String? = null
    var year: Int? = null
    var uuidGrade: UUID? = null
    @NotNull
    var uuidSchool: UUID? = null
}
