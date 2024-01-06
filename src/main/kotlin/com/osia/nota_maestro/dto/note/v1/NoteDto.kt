package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteDto : BaseDto() {
    var grades: List<NoteGradeDto>? = null
    var years: List<Int> = mutableListOf()
}
