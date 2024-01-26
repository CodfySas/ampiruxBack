package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto

class ResourceClassroomDto : BaseDto() {
    var name: String? = null
    var subjects: List<ResourceSubjectDto>? = null
}
