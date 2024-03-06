package com.osia.nota_maestro.dto.accompaniment.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AccompanimentCompleteDto : BaseDto() {
    var uuidClassroom: UUID? = null
    var accompaniments: List<AccompanimentDto>? = null
    var gradeName: String? = null
    var classroomName: String? = null
}
