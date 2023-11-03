package com.osia.nota_maestro.dto.worker.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class WorkerRequest {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
