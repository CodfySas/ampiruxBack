package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import java.util.UUID

class ClassroomsTeachersDto : BaseDto() {
    var name: String? = null
    var uuidTeacher: UUID? = null
    var nameTeacher: String? = null
}
