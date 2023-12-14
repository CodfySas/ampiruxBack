package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto

class SubjectTeachersDto : BaseDto() {
    var name: String? = null
    var grades: List<GradeTeachersDto>? = null
}
