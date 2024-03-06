package com.osia.nota_maestro.dto.accompaniment.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AccompanimentDto : BaseDto() {
    var uuidClassroom: UUID? = null
    var uuidTeacher: UUID? = null
}
