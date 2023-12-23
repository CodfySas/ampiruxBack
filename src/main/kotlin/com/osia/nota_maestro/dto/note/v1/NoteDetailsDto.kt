package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteDetailsDto : BaseDto() {
    var name: String? = null
    var number: Int? = null
    var note: String? = null
}
