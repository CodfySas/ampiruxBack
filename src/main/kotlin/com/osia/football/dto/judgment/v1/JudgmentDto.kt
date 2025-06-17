package com.osia.nota_maestro.dto.judgment.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class JudgmentDto : BaseDto() {
    var name: String? = null
    var entryOn: Int? = null
    var uuidSubject: UUID? = null
    var uuidGrade: UUID? = null
    var period: Int? = null
}
