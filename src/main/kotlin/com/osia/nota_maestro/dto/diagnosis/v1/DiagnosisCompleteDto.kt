package com.osia.nota_maestro.dto.diagnosis.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class DiagnosisCompleteDto : BaseDto() {
    var users: List<UserDiagnosesDto>? = null
    var diagnoses: List<DiagnosisDto>? = null
}
