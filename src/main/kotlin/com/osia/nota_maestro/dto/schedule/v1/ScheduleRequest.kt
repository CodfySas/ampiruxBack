package com.osia.nota_maestro.dto.schedule.v1

import java.util.UUID

class ScheduleRequest {
    var init: String? = null
    var finish: String? = null
    var dayOfWeek: Int? = null
    var uuidGradeSubject: UUID? = null
    var uuidClassroomSubject: UUID? = null
    var uuidSchool: UUID? = null
    var uuidClassroom: UUID? = null
}
