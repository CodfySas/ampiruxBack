package com.osia.nota_maestro.dto.diagnosis.v1

import java.util.UUID

class DiagnosisRequest {
    var name: String? = null
    var lastname: String? = null
    var age: Int? = null
    var uuidStudent: UUID? = null
    var grade: String? = null
    var diagnosis: String? = null
    var uuidSchool: UUID? = null
}
