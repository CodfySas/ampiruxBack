package com.osia.nota_maestro.dto.judgment.v1

import java.util.UUID

class JudgmentRequest {
    var name: String? = null
    var entryOn: Int? = null
    var uuidSubject: UUID? = null
    var uuidGrade: UUID? = null
    var period: Int? = null
}
