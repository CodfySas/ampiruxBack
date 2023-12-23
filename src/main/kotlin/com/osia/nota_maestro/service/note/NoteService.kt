package com.osia.nota_maestro.service.note

import com.osia.nota_maestro.dto.note.v1.NoteDto
import java.util.UUID

interface NoteService {
    fun getMyNotes(teacher: UUID): NoteDto
    fun submitNotes(noteDto: NoteDto, teacher: UUID): NoteDto
}
