package com.osia.nota_maestro.dto.directorStudent.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import java.util.UUID

class DirectorStudentSubjectDto : BaseDto() {
    var name: String? = null
    var def: Double? = null
    var recovery: Double? = null
    var judgment: String? = null
}
