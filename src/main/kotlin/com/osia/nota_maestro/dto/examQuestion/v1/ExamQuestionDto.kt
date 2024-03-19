package com.osia.nota_maestro.dto.examQuestion.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseDto
import java.util.UUID

class ExamQuestionDto : BaseDto() {
    var description: String? = null
    var type: String? = null
    var uuidExam: UUID? = null
    var ordered: Int? = null
    var responses: List<ExamResponseDto>? = null
}
