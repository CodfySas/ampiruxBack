package com.osia.nota_maestro.dto.school.v1

import com.osia.nota_maestro.dto.BaseDto

class SchoolMinDto : BaseDto() {
    var directorName: String? = null
    var directorDni: String? = null
    var directorRole: String? = null
    var signReportType: String? = null
    var certificates: String? = null
    var periodAvailable: String? = null
    var planningType: String? = null
}
