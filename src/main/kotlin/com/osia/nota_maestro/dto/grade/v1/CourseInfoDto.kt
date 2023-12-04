package com.osia.nota_maestro.dto.grade.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomCompleteDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import java.util.UUID
import javax.validation.constraints.NotNull

class CourseInfoDto : BaseDto() {
    var grades: List<GradeCompleteDto>? = null
    var noAssignedStudents: List<UserDto>? = null
}
