package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteStudentDto : BaseDto() {
    var name: String? = null
    var lastname: String? = null
    var subjects: List<NoteSubjectsDto>? = null
    var periods: List<NotePeriodDto>? = mutableListOf()
    var notes: List<NoteDetailsDto>? = mutableListOf()
    var judgment: String = ""
    var def: String = ""
    var recovery: String = ""
}
