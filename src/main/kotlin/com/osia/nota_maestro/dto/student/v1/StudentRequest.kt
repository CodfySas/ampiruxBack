package com.osia.nota_maestro.dto.student.v1

import java.util.UUID
import javax.validation.constraints.NotNull

class StudentRequest {
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
