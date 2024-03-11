package com.osia.nota_maestro.dto.classroomResourceTask.v1

import java.time.LocalDate
import java.util.UUID

class ClassroomResourceTaskRequest {
    var uuidClassroomResource: UUID? = null
    var uuidStudent: UUID? = null
    var uuidClassroomStudent: UUID? = null
    var name: String? = null
    var ext: String? = null
    var hasFile: Boolean? = null
    var description: String? = null
    var submitAt: LocalDate? = null
    var submitAtHour: String? = null
    var note: Double? = null
    var observation: String? = null
}
