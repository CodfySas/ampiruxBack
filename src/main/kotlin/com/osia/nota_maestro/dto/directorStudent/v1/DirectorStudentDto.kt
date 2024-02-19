package com.osia.nota_maestro.dto.directorStudent.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import java.util.UUID

class DirectorStudentDto : BaseDto() {
    var lastname: String? = null
    var name: String? = null
    var uuidClassroomStudent: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var description: Int? = null
    var studentSubjects: List<DirectorStudentSubjectDto>? = null
}
