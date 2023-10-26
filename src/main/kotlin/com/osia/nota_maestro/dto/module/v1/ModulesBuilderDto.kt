package com.osia.nota_maestro.dto.module.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.subModule.v1.SubModuleDto

class ModulesBuilderDto : BaseDto() {
    var name: String? = null
    var order: Int? = null
    var subModules: List<SubModuleDto> = mutableListOf()
}
