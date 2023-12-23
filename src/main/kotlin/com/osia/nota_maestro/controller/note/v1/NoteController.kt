package com.osia.nota_maestro.controller.note.v1

import com.osia.nota_maestro.dto.note.v1.NoteDto
import com.osia.nota_maestro.service.note.NoteService
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

@RestController("note.v1.crud")
@CrossOrigin
@RequestMapping("v1/notes")
@Validated
class NoteController(
    private val noteService: NoteService,
) {
    @GetMapping("/{teacher}")
    fun getById(@PathVariable teacher: UUID): ResponseEntity<NoteDto> {
        return ResponseEntity.ok().body(noteService.getMyNotes(teacher))
    }

    @PostMapping("/submit/{teacher}")
    fun submitNotes(@PathVariable teacher: UUID, @RequestBody notes: NoteDto): ResponseEntity<NoteDto> {
        return ResponseEntity.ok().body(noteService.submitNotes(notes, teacher))
    }
}
