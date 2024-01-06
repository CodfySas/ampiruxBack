package com.osia.nota_maestro.dto.studentSubject.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID
import javax.validation.constraints.NotNull

class StudentSubjectDto : BaseDto() {
    var uuidClassroomStudent: UUID? = null
    var uuidSubject: UUID? = null
    var uuidStudent: UUID? = null
    var period: Int? = null
    var uuidSchool: UUID? = null
    var def: Double? = null
    var recovery: Double? = null
}
