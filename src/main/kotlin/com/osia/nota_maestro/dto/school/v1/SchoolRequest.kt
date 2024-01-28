package com.osia.nota_maestro.dto.school.v1

import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodRequest
import java.time.LocalDateTime

class SchoolRequest {
    var color1: String? = null
    var color2: String? = null
    var name: String? = null
    var active: Boolean? = true
    var expireDate: LocalDateTime? = null
    var shortName: String? = null
    var periods: Int? = null
    var periodList: List<SchoolPeriodRequest>? = null
    var actualYear: Int? = null
    var enabledTeacher: Boolean? = null
    var enabledStudent: Boolean? = null
    var maxNote: Double? = null
    var minNote: Double? = null
    var toLose: Int? = null
    var recoveryType: String? = null
    var enabledFinalRecovery: Boolean? = null
    var directorName: String? = null
    var directorDni: String? = null
    var directorRole: String? = null
    var signReportType: String? = null
    var certificates: String? = null
    var periodAvailable: String? = null
}
