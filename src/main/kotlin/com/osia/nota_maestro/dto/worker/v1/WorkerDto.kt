package com.osia.nota_maestro.dto.worker.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class WorkerDto : BaseDto() {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidCompany: UUID? = null
}
