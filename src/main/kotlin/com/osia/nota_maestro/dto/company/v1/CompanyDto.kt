package com.osia.nota_maestro.dto.company.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDateTime

class CompanyDto : BaseDto() {
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
