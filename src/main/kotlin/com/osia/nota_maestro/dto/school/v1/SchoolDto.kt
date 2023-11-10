package com.osia.nota_maestro.dto.school.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDateTime

class SchoolDto : BaseDto() {
    var color1: String? = null
    var color2: String? = null
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
