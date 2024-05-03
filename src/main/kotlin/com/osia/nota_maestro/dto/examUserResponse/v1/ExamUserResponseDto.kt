package com.osia.nota_maestro.dto.examUserResponse.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ExamUserResponseDto : BaseDto() {
    var uuidExamQuestion: UUID? = null
    var uuidExamResponse: UUID? = null
    var uuidAttempt: UUID? = null
    var correct: Boolean? = null
    var response: String? = null
}
