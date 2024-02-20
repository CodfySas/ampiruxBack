package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class ReportStudentNote : BaseDto() {
    var report: List<NoteSubjectsDto>? = mutableListOf()
    var observations: List<ObservationPeriodDto> = mutableListOf()
    var name: String? = null
    var lastname: String? = null
}
