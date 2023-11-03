package com.osia.nota_maestro.dto.client.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class ClientRequest {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
