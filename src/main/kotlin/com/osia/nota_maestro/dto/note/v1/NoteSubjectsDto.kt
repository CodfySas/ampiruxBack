package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class NoteSubjectsDto : BaseDto() {
    var name: String? = null
    var periods: List<NotePeriodDto>? = mutableListOf()
    var judgments: List<String> = mutableListOf()
    var def: String? = null
    var uuidStudentSubject: UUID? = null
    var recovery: String? = null
}
