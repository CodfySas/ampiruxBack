package com.osia.nota_maestro.dto.planning.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PlanningDto : BaseDto() {
    var day: String? = null
    var position: Int? = null

    var area: String? = null
    var goals: String? = null
    var topic: String? = null
    var activity: String? = null
    var resources: String? = null
    var uuidTeacher: UUID? = null
    var teacherName: String? = null
    var classroom: UUID? = null
    var subject: UUID? = null
    var week: Int? = null

    var dateRange: String? = null
    var observation: String? = null
    var status: String? = null
    var userReview: UUID? = null

    var type: String? = null
}
