package com.osia.nota_maestro.dto.studentSubject.v1

import java.util.UUID

class StudentSubjectRequest {
    var uuidClassroomStudent: UUID? = null
    var uuidSubject: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var uuidSchool: UUID? = null
    var def: Double? = null
    var recovery: Double? = null
    var judgment: String? = null
}
