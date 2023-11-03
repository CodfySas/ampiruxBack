package com.osia.nota_maestro.dto.calendar.v1

import com.osia.nota_maestro.model.enums.TaskTypeEnum
import java.time.LocalDateTime
import java.util.UUID

class CalendarTaskRequest {
    var uuidSchool: UUID? = null
    var scheduleInit: LocalDateTime? = null
    var hour: String? = null
    var scheduleFinish: LocalDateTime? = null
    var taskType: TaskTypeEnum? = null
    var description: String? = null
    var assignedTo: UUID? = null
    var uuidClient: UUID? = null
}
