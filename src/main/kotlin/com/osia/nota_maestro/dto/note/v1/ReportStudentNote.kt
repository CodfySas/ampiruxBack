package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class ReportStudentNote : BaseDto() {
    var report: List<NoteSubjectsDto>? = mutableListOf()
    var periodDef: List<NotePeriodDto>? = mutableListOf()
    var observations: List<ObservationPeriodDto> = mutableListOf()
    var accompaniments: List<AccompanimentPeriodDto> = mutableListOf()
    var name: String? = null
    var lastname: String? = null
    var director: String? = null
    var classroom: String? = null
    var position: Int? = null
    var prom: String? = null
    var promBasic: String? = null
}
