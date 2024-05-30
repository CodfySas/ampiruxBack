package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionDto

class ReportStudentNote : BaseDto() {
    var report: List<NoteSubjectsDto>? = mutableListOf()
    var periodDef: List<NotePeriodDto>? = mutableListOf()
    var observations: List<ObservationPeriodDto> = mutableListOf()
    var accompaniments: List<AccompanimentPeriodDto> = mutableListOf()
    var positions: List<StudentPositionDto> = mutableListOf()
    var name: String? = null
    var lastname: String? = null
    var director: String? = null
    var classroom: String? = null
    var position: Int? = null
    var prom: String? = null
    var promBasic: String? = null
    var noteType: String? = null
}
