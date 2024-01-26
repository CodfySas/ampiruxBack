package com.osia.nota_maestro.controller.note.v1

import com.osia.nota_maestro.dto.note.v1.NoteDto
import com.osia.nota_maestro.dto.resources.v1.ResourceDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.service.note.NoteService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
        return ResponseEntity.ok().body(noteService.getMyNotes(teacher, ResourceRequest()))
    }

    @GetMapping("/years")
    fun getMyYears(@RequestHeader school: UUID): ResponseEntity<List<Int>> {
        return ResponseEntity.ok().body(noteService.getYears(school))
    }

    @GetMapping("/resources/{teacher}/{year}")
    fun getMyResources(@PathVariable teacher: UUID, @PathVariable year: Int): ResponseEntity<ResourceDto> {
        return ResponseEntity.ok().body(noteService.getMyResources(teacher, year))
    }

    @PostMapping("/{teacher}")
    fun getMyNotes(@PathVariable teacher: UUID, @RequestBody request: ResourceRequest): ResponseEntity<NoteDto> {
        return ResponseEntity.ok().body(noteService.getMyNotes(teacher, request))
    }

    @PostMapping("/{teacher}/year/{year}/{type}")
    fun getArchive(@PathVariable teacher: UUID, @PathVariable year: Int, @PathVariable type: String, @RequestBody request: ResourceRequest): ResponseEntity<NoteDto> {
        return ResponseEntity.ok().body(noteService.getMyNotesArchive(teacher, year, type, request))
    }

    @PostMapping("/submit/{teacher}")
    fun submitNotes(@PathVariable teacher: UUID, @RequestBody notes: List<NoteDto>): ResponseEntity<List<NoteDto>> {
        return ResponseEntity.ok().body(noteService.submitNotes(notes, teacher))
    }
}
