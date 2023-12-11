package com.osia.nota_maestro.dto.subject.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class SubjectRequest {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
