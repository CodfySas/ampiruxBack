package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteSubjectsDto : BaseDto() {
    var name: String? = null
    var notes: List<NoteDetailsDto>? = mutableListOf()
}
