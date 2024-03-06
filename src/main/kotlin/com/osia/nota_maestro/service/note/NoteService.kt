package com.osia.nota_maestro.service.note

import com.osia.nota_maestro.dto.note.v1.NoteDto
import com.osia.nota_maestro.dto.resources.v1.MyAssignmentDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.ClassroomStudent
import java.util.UUID

interface NoteService {
    fun getMyNotes(teacher: UUID, request: ResourceRequest): NoteDto
    fun getMyNotesArchive(teacher: UUID, year: Int, type: String, request: ResourceRequest): NoteDto
    fun submitNotes(notesDto: List<NoteDto>, teacher: UUID): List<NoteDto>
    fun getYears(schoolUUID: UUID): List<Int>
    fun setNotes(classrooms: List<Classroom>, schoolUUID: UUID, judgmentsSubmited: List<StudentSubjectDto>)
    fun setPositions(classroomStudents: List<ClassroomStudent>, allSs: List<StudentSubjectDto>)
    fun getMyResources(teacher: UUID, year: Int): MyAssignmentDto
}
