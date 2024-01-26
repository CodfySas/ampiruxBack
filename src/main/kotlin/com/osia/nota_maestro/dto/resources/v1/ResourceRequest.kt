package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ResourceRequest : BaseDto() {
    var period: Int = 0
    var subject: UUID = UUID.randomUUID()
    var grade: UUID = UUID.randomUUID()
    var classroom: UUID = UUID.randomUUID()
}
