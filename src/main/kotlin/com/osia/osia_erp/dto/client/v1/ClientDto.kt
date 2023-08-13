package com.osia.osia_erp.dto.client.v1

import com.osia.osia_erp.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class ClientDto : BaseDto() {
    var name: String? = null
    var id: String? = null
    var phone: String? = null
    @NotNull
    var uuidCompany: UUID? = null
}
