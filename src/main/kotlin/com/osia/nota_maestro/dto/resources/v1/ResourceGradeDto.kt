package com.osia.nota_maestro.dto.resources.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto

class ResourceGradeDto : BaseDto() {
    var name: String? = null
    var classrooms: List<ResourceClassroomDto>? = null
    var teachers: List<UserDto> = mutableListOf()
    var attendanceType: String? = null
    var noteType: String? = null
    var preInfoType: String? = null
}
