package com.osia.nota_maestro.service.report

import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import java.util.UUID

interface ReportService {
    fun getByStudent(student: UUID): List<NoteSubjectsDto>
}
