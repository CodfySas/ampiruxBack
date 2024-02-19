package com.osia.nota_maestro.dto.directorStudent.v1

import java.util.UUID

class DirectorStudentRequest {
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var description: String? = null
}
