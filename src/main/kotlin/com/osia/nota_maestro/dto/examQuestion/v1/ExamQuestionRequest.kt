package com.osia.nota_maestro.dto.examQuestion.v1

import java.util.UUID

class ExamQuestionRequest {
    var description: String? = null
    var type: String? = null
    var uuidExam: UUID? = null
    var ordered: Int? = null
}
