package com.osia.nota_maestro.dto.preliminary.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PreliminaryAllDto : BaseDto() {
    var uuidStudent: UUID? = null
    var uuidClassroom: UUID? = null
    var student: String? = null
    var period: Int? = null
    var preliminaries: List<PreliminaryDto>? = mutableListOf()
}
