package com.osia.nota_maestro.dto.attendance.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class AttendanceCompleteDto : BaseDto() {
    var name: String? = null
    var lastname: String? = null
    var attendances: List<AttendanceDto> = mutableListOf()
}
