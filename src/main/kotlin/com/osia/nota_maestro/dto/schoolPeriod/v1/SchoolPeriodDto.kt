package com.osia.nota_maestro.dto.schoolPeriod.v1

import com.osia.nota_maestro.dto.BaseDto
import java.time.LocalDateTime
import java.util.UUID

class SchoolPeriodDto : BaseDto() {
    var number: Int? = null
    var init: LocalDateTime? = null
    var finish: LocalDateTime? = null
    var uuidSchool: UUID? = null
    var actualYear: Int? = null
    var recovery: Boolean? = null
}
