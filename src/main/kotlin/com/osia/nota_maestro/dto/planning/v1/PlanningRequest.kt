package com.osia.nota_maestro.dto.planning.v1

import java.util.UUID

class PlanningRequest {
    var day: String? = null
    var position: Int? = null

    var area: String? = null
    var goals: String? = null
    var topic: String? = null
    var activity: String? = null
    var resources: String? = null

    var classroom: UUID? = null
    var subject: UUID? = null
    var week: Int? = null

    var dateRange: String? = null
    var observation: String? = null
    var status: String? = null
    var userReview: UUID? = null
}
