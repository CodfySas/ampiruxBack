package com.osia.nota_maestro.dto.classroomResource.v1

import java.time.LocalDate
import java.util.UUID

class ClassroomResourceRequest {
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
    var initTime: LocalDate? = null
    var initHour: String? = null
    var attempts: Int? = null
}
