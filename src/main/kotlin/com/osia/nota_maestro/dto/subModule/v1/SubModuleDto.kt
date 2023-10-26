package com.osia.nota_maestro.dto.subModule.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class SubModuleDto : BaseDto() {
    var name: String? = null
    var routeName: String? = null
    var uuidModule: UUID? = null
    var order: Int? = null
}
