package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class NoteSubjectsDto : BaseDto() {
    var name: String? = null
    var periods: List<NotePeriodDto>? = mutableListOf()
    var children: List<NoteSubjectsDto>? = mutableListOf()
    var judgments: List<String> = mutableListOf()
    var def: String = ""
    var basic: String = ""
    var recoveryBasic: String = ""
    var color: String = ""
    var uuidStudentSubject: UUID? = null
    var recovery: String? = null
    var isParent: Boolean? = false
    var uuidParent: UUID? = null
}
