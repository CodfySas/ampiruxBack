package com.osia.nota_maestro.dto.calendar.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.model.enums.TaskTypeEnum
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class CalendarTaskDto : BaseDto() {
    var uuidSchool: UUID? = null
    var day: LocalDate? = null
    var hourInit: String? = null
    var hourFinish: String? = null
    var taskType: TaskTypeEnum? = null
    var description: String? = null
    var assignedTo: UUID? = null
    var uuidResource: UUID? = null
}
