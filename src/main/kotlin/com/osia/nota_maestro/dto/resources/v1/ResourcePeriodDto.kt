package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto

class ResourcePeriodDto : BaseDto() {
    var number: Int? = null
    var grades: List<ResourceGradeDto>? = null
}
