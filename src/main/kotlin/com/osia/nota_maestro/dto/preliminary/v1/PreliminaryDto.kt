package com.osia.nota_maestro.dto.preliminary.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class PreliminaryDto : BaseDto() {
    var target: String? = null
    var success: Boolean? = null
    var aspect: String? = null
    var observations: String? = null
    var uuidSubject: UUID? = null
    var subject: String? = null
    var canEdit: Boolean? = false
}
