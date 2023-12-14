package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto

class CompleteSubjectsTeachersDto : BaseDto() {
    var teachers: List<UserDto>? = null
    var subjects: List<SubjectTeachersDto>? = null
}
