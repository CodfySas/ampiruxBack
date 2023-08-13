package com.osia.osia_erp.dto.client.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class ClientRequest {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidCompany: UUID? = null
}
