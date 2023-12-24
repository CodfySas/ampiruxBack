package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NotePeriodDto : BaseDto() {
    var number: Int? = null
    var judgment: String? = null
    var notes: List<NoteDetailsDto>? = mutableListOf()
}
