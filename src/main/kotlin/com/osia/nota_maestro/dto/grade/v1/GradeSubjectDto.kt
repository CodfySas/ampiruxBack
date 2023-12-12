package com.osia.nota_maestro.dto.grade.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomCompleteDto
import com.osia.nota_maestro.dto.subject.v1.SubjectDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import java.util.UUID
import javax.validation.constraints.NotNull

class GradeSubjectDto : BaseDto() {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
    var subjects: List<SubjectDto> = mutableListOf()
}
