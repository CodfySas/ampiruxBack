package com.osia.nota_maestro.dto.gradeSubject.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class GradeSubjectRequest {
    var uuidGrade: UUID? = null
    var uuidSubject: UUID? = null
    var uuidTeacher: UUID? = null
}
