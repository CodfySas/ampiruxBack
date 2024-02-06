package com.osia.nota_maestro.service.report

import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.note.v1.ReportStudentNote
import java.util.UUID

interface ReportService {
    fun getByStudent(student: UUID): List<NoteSubjectsDto>
    fun getByMultipleStudent(list: List<UUID>): List<ReportStudentNote>
}
