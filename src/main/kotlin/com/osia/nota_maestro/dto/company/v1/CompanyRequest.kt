package com.osia.nota_maestro.dto.company.v1

import java.time.LocalDateTime

class CompanyRequest {
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
