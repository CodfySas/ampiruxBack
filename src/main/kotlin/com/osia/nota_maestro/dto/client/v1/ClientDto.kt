package com.osia.nota_maestro.dto.client.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class ClientDto : BaseDto() {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidCompany: UUID? = null
}
