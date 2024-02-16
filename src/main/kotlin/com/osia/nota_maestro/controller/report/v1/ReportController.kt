package com.osia.nota_maestro.controller.report.v1

import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.note.v1.ReportStudentNote
import com.osia.nota_maestro.service.report.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("report.v1.crud")
@CrossOrigin
@RequestMapping("v1/report")
@Validated
class ReportController(
    private val reportService: ReportService,
) {
    @GetMapping("/student/{uuid}")
    fun getByStudent(@PathVariable uuid: UUID): ResponseEntity<List<NoteSubjectsDto>> {
        return ResponseEntity.ok().body(reportService.getByStudent(uuid))
    }

    @PostMapping("/all")
    fun getByMultiple(@RequestBody list: List<UUID>): ResponseEntity<List<ReportStudentNote>> {
        return ResponseEntity.ok().body(reportService.getByMultipleStudent(list))
    }
}
