package com.osia.osia_erp.dto.company.v1

import java.time.LocalDateTime

class CompanyRequest {
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
