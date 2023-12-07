package com.osia.nota_maestro.dto.grade.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto

class CourseInfoDto : BaseDto() {
    var grades: List<GradeCompleteDto>? = null
    var noAssignedStudents: List<UserDto>? = null
}
