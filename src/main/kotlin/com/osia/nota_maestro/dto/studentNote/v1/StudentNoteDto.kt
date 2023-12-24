package com.osia.nota_maestro.dto.studentNote.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class StudentNoteDto : BaseDto() {
    var uuidStudent: UUID? = null
    var number: Int? = null
    var noteName: String? = null
    var note: Double? = null
    var uuidClassroomStudent: UUID? = null
    var uuidSubject: UUID? = null
    var period: Int? = null
}
