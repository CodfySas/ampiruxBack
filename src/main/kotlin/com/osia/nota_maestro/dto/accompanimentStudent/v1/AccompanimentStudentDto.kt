package com.osia.nota_maestro.dto.accompanimentStudent.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AccompanimentStudentDto : BaseDto() {
    var lastname: String? = null
    var name: String? = null
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var description: String? = null
    var studentSubjects: List<AccompanimentStudentSubjectDto>? = null
}
