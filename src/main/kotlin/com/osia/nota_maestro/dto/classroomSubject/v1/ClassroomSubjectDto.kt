package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ClassroomSubjectDto : BaseDto() {
    var uuidTeacher: UUID? = null
    var uuidSubject: UUID? = null
    var uuidClassroom: UUID? = null
}
