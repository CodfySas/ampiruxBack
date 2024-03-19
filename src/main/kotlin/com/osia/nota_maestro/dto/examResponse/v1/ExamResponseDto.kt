package com.osia.nota_maestro.dto.examResponse.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ExamResponseDto : BaseDto() {
    var description: String? = null
    var correct: Boolean? = null
    var uuidExamQuestion: UUID? = null
}
