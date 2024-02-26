package com.osia.nota_maestro.dto.grade.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class GradeRequest {
    var name: String? = null
    @NotNull
    var uuidSchool: UUID? = null
    var ordered: Int? = null
    var hourInit: String? = null
    var hourFinish: String? = null
    var recessAInit: String? = null
    var recessAFinish: String? = null
    var recessBInit: String? = null
    var recessBFinish: String? = null
    var duration: Int? = null
    var recess: Int? = null
}
