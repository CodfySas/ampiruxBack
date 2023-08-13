package com.osia.osia_erp.dto.subModule.v1

import com.osia.osia_erp.dto.BaseDto
import java.util.UUID

class SubModuleDto : BaseDto() {
    var name: String? = null
    var routeName: String? = null
    var uuidModule: UUID? = null
    var order: Int? = null
}
