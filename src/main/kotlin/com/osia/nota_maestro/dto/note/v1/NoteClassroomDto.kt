package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto

class NoteClassroomDto : BaseDto() {
    var name: String? = null
    var students: List<NoteStudentDto>? = null
}
