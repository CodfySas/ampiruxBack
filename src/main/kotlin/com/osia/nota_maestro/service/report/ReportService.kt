package com.osia.nota_maestro.service.report

import com.osia.nota_maestro.dto.note.v1.ReportStudentNote
import java.util.UUID

interface ReportService {
    fun getByStudent(student: UUID): ReportStudentNote
    fun getByMultipleStudent(list: List<UUID>): List<ReportStudentNote>
}
