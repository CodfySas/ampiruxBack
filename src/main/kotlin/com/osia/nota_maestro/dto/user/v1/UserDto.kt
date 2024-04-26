package com.osia.nota_maestro.dto.user.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodDto
import java.util.UUID

class UserDto : BaseDto() {
    var color1: String? = null
    var color2: String? = null
    var username: String? = null
    var name: String? = null
    var role: String? = null
    var token: String? = null
    var uuidSchool: UUID? = null
    var schoolName: String? = null
    var dni: String? = null
    var documentType: String? = null
    var lastname: String? = null
    var uuidRole: UUID? = null
    var shortName: String? = null
    var phone: String? = null
    var address: String? = null
    var email: String? = null
    var actualGrade: UUID? = null
    var periods: Int? = null
    var periodList: List<SchoolPeriodDto>? = null
    var actualYear: Int? = null
    var enabledTeacher: Boolean? = null
    var enabledStudent: Boolean? = null
    var maxNote: Double? = null
    var minNote: Double? = null
    var toLose: Int? = null
    var recoveryType: String? = null
    var enabledFinalRecovery: Boolean? = null
    var parentName: String? = null
    var parentPhone: String? = null
    var parentEmail: String? = null
    var parentAddress: String? = null
    var parentVulnerability: String? = null
    var grade: String? = null
    var classroom: String? = null
    var active: Boolean? = null
    var superUser: Boolean? = null
    var reportType: String? = null
    var planningType: String? = null
}
