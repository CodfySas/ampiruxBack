package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto

class SubjectTeachersDto : BaseDto() {
    var name: String? = null
    var grades: List<GradeTeachersDto>? = null
}
