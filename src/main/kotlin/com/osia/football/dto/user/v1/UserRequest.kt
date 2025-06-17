package com.osia.nota_maestro.dto.user.v1

import java.util.UUID

class UserRequest {
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var lastname: String? = null
    var documentType: String? = null
    var dni: String? = null
    var role: String? = null
    var uuidSchool: UUID? = null
    var schoolName: String? = null

    var phone: String? = null
    var email: String? = null
    var address: String? = null
    var actualGrade: UUID? = null

    var parentName: String? = null
    var parentPhone: String? = null
    var parentEmail: String? = null
    var parentAddress: String? = null
    var parentVulnerability: String? = null

    var active: Boolean? = null
    var superUser: Boolean? = null
}
