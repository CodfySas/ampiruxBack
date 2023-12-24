package com.osia.nota_maestro.dto.judgment.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class JudgmentRequest {
    var name: String? = null
    var uuidClassroomStudent: UUID? = null
    var uuidSubject: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
}
