package com.osia.template.dto.judgment.v1

import com.osia.template.dto.BaseDto
import java.util.UUID

class JudgmentDto : BaseDto() {
    var name: String? = null
    var entryOn: Int? = null
    var uuidSubject: UUID? = null
    var uuidGrade: UUID? = null
    var period: Int? = null
}
