package com.osia.nota_maestro.dto.grade.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomCompleteDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import java.util.UUID
import javax.validation.constraints.NotNull

class GradeCompleteDto : BaseDto() {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
    var classrooms: List<ClassroomCompleteDto> = mutableListOf()
    var noAssignedStudents: List<UserDto>? = null
}
