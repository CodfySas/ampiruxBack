package com.osia.nota_maestro.dto.classroomResourceTask.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ClassroomResourceTaskDto : BaseDto() {
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
    var noteText: String? = null
    var observation: String? = null
    var sname: String? = null
    var slastname: String? = null
}
