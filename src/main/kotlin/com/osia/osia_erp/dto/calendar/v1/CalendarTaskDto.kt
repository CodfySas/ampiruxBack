package com.osia.osia_erp.dto.calendar.v1

import com.osia.osia_erp.dto.BaseDto
import com.osia.osia_erp.model.enums.TaskTypeEnum
import java.time.LocalDateTime
import java.util.UUID

class CalendarTaskDto : BaseDto() {
    var uuidCompany: UUID? = null
    var scheduleInit: LocalDateTime? = null
    var hour: String? = null
    var scheduleFinish: LocalDateTime? = null
    var taskType: TaskTypeEnum? = null
    var description: String? = null
    var assignedTo: UUID? = null
    var uuidClient: UUID? = null
}
