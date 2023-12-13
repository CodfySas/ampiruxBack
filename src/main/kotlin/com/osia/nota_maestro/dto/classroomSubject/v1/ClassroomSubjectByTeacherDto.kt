package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ClassroomSubjectByTeacherDto : BaseDto() {
    // subject name
    var name: String = ""
    var uuidTeacher: UUID? = null
    var teacherName: String? = null
}
