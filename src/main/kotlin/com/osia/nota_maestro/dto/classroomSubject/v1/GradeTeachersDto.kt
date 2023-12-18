package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto

class GradeTeachersDto : BaseDto() {
    var name: String? = null
    var classrooms: List<ClassroomsTeachersDto>? = null
}
