package com.osia.nota_maestro.dto.certificate.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDateTime
import java.util.UUID

class CertificateDto : BaseDto() {
    var type: String? = null
    var approved: Boolean? = null
    var uuidUser: UUID? = null
    var uuidSchool: UUID? = null
    var approvedAt: LocalDateTime? = null
    var actualGrade: String? = null
    var initTime: String? = null
    var position: String? = null
    var status: String? = null
    var term: String? = null
    var salary: String? = null
    var user: String? = null
    var role: String? = null
}
