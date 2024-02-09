package com.osia.nota_maestro.dto.note.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class NoteDto : BaseDto() {
    var grades: List<NoteGradeDto>? = null
    var years: List<Int> = mutableListOf()
    var students: List<NoteStudentDto>? = null
    var period: Int = 0
    var subject: UUID = UUID.randomUUID()
    var grade: UUID = UUID.randomUUID()
    var classroom: UUID = UUID.randomUUID()
    var judgments: List<String>? = null
}
