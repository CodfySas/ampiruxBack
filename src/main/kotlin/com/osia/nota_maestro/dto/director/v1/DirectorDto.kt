package com.osia.nota_maestro.dto.director.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class DirectorDto : BaseDto() {
    var uuidClassroom: UUID? = null
    var uuidTeacher: UUID? = null
}
