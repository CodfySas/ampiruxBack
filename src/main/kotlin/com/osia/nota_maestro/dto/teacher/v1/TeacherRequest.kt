package com.osia.nota_maestro.dto.teacher.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class TeacherRequest {
    var name: String? = null
    var lastname: String? = null
    var dni: String? = null
    var documentType: String? = null
    var phone: String? = null
    var email: String? = null
    var address: String? = null
    @NotNull
    var uuidSchool: UUID? = null
}
