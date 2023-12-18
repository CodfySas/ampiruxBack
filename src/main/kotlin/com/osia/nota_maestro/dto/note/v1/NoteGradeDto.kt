package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteGradeDto : BaseDto() {
    var name: String? = null
    var classrooms: List<NoteClassroomDto>? = null
}
