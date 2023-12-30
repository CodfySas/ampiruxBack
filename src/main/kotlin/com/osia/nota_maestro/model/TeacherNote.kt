package com.osia.nota_maestro.model

import java.util.UUID

data class TeacherNote(
    var uuidTeacher: UUID? = null,
    var notes: Int? = null,
)
