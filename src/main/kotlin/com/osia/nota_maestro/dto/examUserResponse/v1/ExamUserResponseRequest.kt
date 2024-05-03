package com.osia.nota_maestro.dto.examUserResponse.v1

import java.util.UUID

class ExamUserResponseRequest {
    var uuidExamQuestion: UUID? = null
    var uuidExamResponse: UUID? = null
    var uuidAttempt: UUID? = null
    var correct: Boolean? = null
    var response: String? = null
}
