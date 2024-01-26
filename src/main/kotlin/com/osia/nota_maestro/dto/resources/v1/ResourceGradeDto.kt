package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto

class ResourceGradeDto : BaseDto() {
    var name: String? = null
    var classrooms: List<ResourceClassroomDto>? = null
}
