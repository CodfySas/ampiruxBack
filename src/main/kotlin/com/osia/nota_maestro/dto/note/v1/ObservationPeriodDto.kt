package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ObservationPeriodDto : BaseDto() {
    var period: Int? = null
    var description: String = ""
}
