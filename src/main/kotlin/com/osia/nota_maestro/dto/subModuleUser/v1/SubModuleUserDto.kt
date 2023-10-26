package com.osia.nota_maestro.dto.subModuleUser.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class SubModuleUserDto : BaseDto() {
    var uuidUser: UUID? = null
    var uuidSubModule: UUID? = null
}
