package com.osia.osia_erp.dto.module.v1

import com.osia.osia_erp.dto.BaseDto
import com.osia.osia_erp.dto.subModule.v1.SubModuleDto

class ModulesBuilderDto : BaseDto() {
    var name: String? = null
    var order: Int? = null
    var subModules: List<SubModuleDto> = mutableListOf()
}
