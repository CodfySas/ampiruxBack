package com.osia.osia_erp.dto.subModuleUser.v1

import com.osia.osia_erp.dto.BaseDto
import java.util.UUID

class SubModuleUserDto : BaseDto() {
    var uuidUser: UUID? = null
    var uuidSubModule: UUID? = null
}
