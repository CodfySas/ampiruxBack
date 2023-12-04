package com.osia.nota_maestro.dto.classroom.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import java.util.UUID
import javax.validation.constraints.NotNull

class ClassroomCompleteDto : BaseDto() {
    var name: String? = null
    var year: Int? = null
    var uuidGrade: UUID? = null
    var students: List<UserDto>? = mutableListOf()
    @NotNull
    var uuidSchool: UUID? = null
}
