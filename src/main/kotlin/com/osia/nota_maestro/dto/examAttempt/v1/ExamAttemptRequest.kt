package com.osia.nota_maestro.dto.examAttempt.v1

import java.util.UUID

class ExamAttemptRequest {
    var uuidStudent: UUID? = null
    var uuidExam: UUID? = null
    var note: Double? = null
    var observation: String? = null
}
