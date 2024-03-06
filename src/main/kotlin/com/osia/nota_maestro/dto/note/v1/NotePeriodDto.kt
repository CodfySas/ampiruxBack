package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class NotePeriodDto : BaseDto() {
    var number: Int? = null
    var judgment: String? = null
    var uuidJudgment: UUID? = null
    var notes: List<NoteDetailsDto>? = mutableListOf()
    var defi: Double? = null
    var uuidStudentSubject: UUID? = null
    var recovery: String? = null
    var def: String = ""
    var basic: String = ""
    var recoveryBasic: String = ""
    var color: String = ""
}
