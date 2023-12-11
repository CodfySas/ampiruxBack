package com.osia.nota_maestro.dto.subject.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class SubjectDto : BaseDto() {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
