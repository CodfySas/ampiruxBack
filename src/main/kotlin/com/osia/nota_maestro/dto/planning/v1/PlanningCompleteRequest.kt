package com.osia.nota_maestro.dto.planning.v1

import java.util.UUID

class PlanningCompleteRequest {
    var classroom: UUID? = null
    var subject: UUID? = null
    var week: Int? = null
    var teacher: UUID? = null
    var my: UUID? = null
    var area: String? = null
}
