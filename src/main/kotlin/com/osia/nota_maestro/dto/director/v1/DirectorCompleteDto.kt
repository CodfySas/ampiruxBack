package com.osia.nota_maestro.dto.director.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class DirectorCompleteDto : BaseDto() {
    var uuidClassroom: UUID? = null
    var uuidTeacher: UUID? = null
    var gradeName: String? = null
    var classroomName: String? = null
}
