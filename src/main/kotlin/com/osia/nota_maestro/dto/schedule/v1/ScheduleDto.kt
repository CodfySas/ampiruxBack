package com.osia.nota_maestro.dto.schedule.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ScheduleDto : BaseDto() {
    var init: String? = null
    var finish: String? = null
    var dayOfWeek: Int? = null
    var uuidGradeSubject: UUID? = null
    var uuidSubject: UUID? = null
    var subjectName: String? = null
    var uuidClassroomSubject: UUID? = null
    var uuidClassroom: UUID? = null
    var uuidSchool: UUID? = null
}
