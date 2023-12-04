package com.osia.nota_maestro.dto.subModule.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class SubModuleDto : BaseDto() {
    var name: String? = null
    var routeName: String? = null
    var uuidModule: UUID? = null
    var ordered: Int? = null
    var parent: Boolean? = null
    var parentUuid: UUID? = null
    var childs: List<SubModuleDto> = mutableListOf()
}
