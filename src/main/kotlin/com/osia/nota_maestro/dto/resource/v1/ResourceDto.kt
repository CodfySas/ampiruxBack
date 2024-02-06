package com.osia.nota_maestro.dto.resource.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ResourceDto : BaseDto() {
    var name: String? = null
    var uuidSchool: UUID? = null
}
