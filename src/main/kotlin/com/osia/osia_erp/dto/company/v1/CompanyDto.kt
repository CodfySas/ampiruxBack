package com.osia.osia_erp.dto.company.v1

import com.osia.osia_erp.dto.BaseDto
import java.time.LocalDateTime

class CompanyDto : BaseDto() {
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
}
