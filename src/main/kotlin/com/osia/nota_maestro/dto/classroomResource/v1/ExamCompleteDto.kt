package com.osia.nota_maestro.dto.classroomResource.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import java.time.LocalDate
import java.util.UUID

class ExamCompleteDto : BaseDto() {
    var name: String? = null
    var finishTime: LocalDate? = null
    var durationTime: Int? = null
    var lastHour: String? = null
    var initTime: LocalDate? = null
    var initHour: String? = null
    var attempts: Int? = null
    var questions: List<ExamQuestionDto>? = null
    var attemptsDone: List<ExamQuestionDto> = mutableListOf()
}
