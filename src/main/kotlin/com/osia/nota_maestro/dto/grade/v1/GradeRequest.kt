package com.osia.nota_maestro.dto.grade.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class GradeRequest {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
