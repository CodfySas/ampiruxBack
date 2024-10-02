package com.osia.nota_maestro.dto.preliminary.v1

import java.util.UUID

class PreliminaryRequest {
    var target: String? = null
    var success: Boolean? = null
    var aspect: String? = null
    var observations: String? = null
    var uuidClassroom: UUID? = null
    var uuidSubject: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
}
