package com.osia.nota_maestro.dto.gradeSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class GradeSubjectDto : BaseDto() {
    var uuidGrade: UUID? = null
    var uuidSubject: UUID? = null
    var uuidTeacher: UUID? = null
}
