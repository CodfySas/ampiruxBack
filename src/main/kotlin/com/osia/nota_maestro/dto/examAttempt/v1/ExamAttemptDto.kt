package com.osia.nota_maestro.dto.examAttempt.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseDto
import java.util.UUID

class ExamAttemptDto : BaseDto() {
    var uuidStudent: UUID? = null
    var uuidExam: UUID? = null
    var note: Double? = null
    var observation: String? = null
    var examUserResponses: List<ExamUserResponseDto> = mutableListOf()
}
