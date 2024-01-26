package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto

class ResourceDto : BaseDto() {
    var periods: List<ResourcePeriodDto>? = null
}
