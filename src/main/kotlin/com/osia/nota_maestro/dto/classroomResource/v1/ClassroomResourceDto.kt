package com.osia.nota_maestro.dto.classroomResource.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ClassroomResourceDto : BaseDto() {
    var name: String? = null
    var type: String? = null
    var description: String? = null
    var classroom: UUID? = null
    var subject: UUID? = null
    var period: Int? = null
    var finishTime: LocalDate? = null
    var durationTime: Int? = null
    var lastHour: String? = null
    var hasFile: Boolean? = null
    var ext: String? = null
}
