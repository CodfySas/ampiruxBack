package com.osia.nota_maestro.dto.accompanimentStudent.v1

import java.util.UUID

class AccompanimentStudentRequest {
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var description: String? = null
}
