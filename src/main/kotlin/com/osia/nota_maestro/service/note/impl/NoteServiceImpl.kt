package com.osia.nota_maestro.service.note.impl

import com.osia.nota_maestro.dto.judgment.v1.JudgmentRequest
import com.osia.nota_maestro.dto.note.v1.NoteClassroomDto
import com.osia.nota_maestro.dto.note.v1.NoteDetailsDto
import com.osia.nota_maestro.dto.note.v1.NoteDto
import com.osia.nota_maestro.dto.note.v1.NoteGradeDto
import com.osia.nota_maestro.dto.note.v1.NotePeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteStudentDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteRequest
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.judgment.JudgmentRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.note.NoteService
import com.osia.nota_maestro.service.studentNote.StudentNoteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service("note.crud_service")
@Transactional
class NoteServiceImpl(
    private val gradeRepository: GradeRepository,
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val studentNoteRepository: StudentNoteRepository,
    private val studentNoteService: StudentNoteService,
    private val judgmentRepository: JudgmentRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository
) : NoteService {

    override fun getMyNotes(teacher: UUID): NoteDto {
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(classroomSubjects.mapNotNull { it.uuidClassroom }, LocalDateTime.now().year)
        return returnNotes(classrooms, classroomSubjects, false)
    }

    override fun getMyNotesArchive(teacher: UUID, year: Int): NoteDto {
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(classroomSubjects.mapNotNull { it.uuidClassroom }, year)
        return returnNotes(classrooms, classroomSubjects, true)
    }

    private fun returnNotes(
        classrooms: List<Classroom>,
        classroomSubjects: List<ClassroomSubject>,
        includeAllPeriods: Boolean
    ): NoteDto {
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })
        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        val judgments = judgmentRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val periods = if(!includeAllPeriods) {
            grades.firstOrNull()?.uuidSchool?.let { schoolPeriodRepository.findAllByUuidSchool(it) }?.filter { it.init != null && it.finish != null && it.init!! <= LocalDateTime.now().plusDays(1) && it.finish!!.plusDays(1) >= LocalDateTime.now() } ?: mutableListOf()
        }else {
            grades.firstOrNull()?.uuidSchool?.let { schoolPeriodRepository.findAllByUuidSchool(it) }?.filter { it.init != null && it.finish != null } ?: mutableListOf()
        }

        return NoteDto().apply {
            this.grades = grades.map { g ->
                NoteGradeDto().apply {
                    this.uuid = g.uuid
                    this.name = g.name
                    this.classrooms = classrooms.filter { c -> c.uuidGrade == g.uuid }.map { c ->
                        NoteClassroomDto().apply {
                            this.uuid = c.uuid
                            this.name = c.name
                            this.students = classroomStudents.filter { cs -> cs.uuidClassroom == c.uuid }.map { cs ->
                                NoteStudentDto().apply {
                                    val student = students.first { it.uuid == cs.uuidStudent }
                                    this.uuid = cs.uuidStudent
                                    this.name = student.name
                                    this.lastname = student.lastname
                                    this.subjects =
                                        classroomSubjects.filter { cx -> cx.uuidClassroom == cs.uuidClassroom }
                                            .map { cx ->
                                                NoteSubjectsDto().apply {
                                                    this.uuid = cx.uuidSubject
                                                    this.name = subjects.first { it.uuid == cx.uuidSubject }.name

                                                    this.periods = periods.map { p ->
                                                        val maxNote = studentNoteRepository.getNoteMAx(
                                                            classroomStudents.mapNotNull { it.uuid },
                                                            p.number!!,
                                                            cx.uuidSubject!!
                                                        )
                                                        NotePeriodDto().apply {
                                                            this.judgment =
                                                                judgments.firstOrNull { j -> j.uuidSubject == cx.uuidSubject && j.uuidClassroomStudent == cx.uuid && j.period == p.number }?.name
                                                                    ?: ""
                                                            this.number = p.number
                                                            if (studentNotes.none { sn -> sn.uuidClassroomStudent == cs.uuid && sn.uuidSubject == cx.uuidSubject && sn.period == p.number }) {
                                                                val emptyNotes = mutableListOf<NoteDetailsDto>()
                                                                for (i in 1..maxNote) {
                                                                    emptyNotes.add(
                                                                        NoteDetailsDto().apply {
                                                                            this.number = 0
                                                                        }
                                                                    )
                                                                }
                                                                this.notes = emptyNotes
                                                            } else {
                                                                this.notes =
                                                                    studentNotes.filter { sn -> sn.uuidClassroomStudent == cs.uuid && sn.uuidSubject == cx.uuidSubject && sn.period == p.number }
                                                                        .sortedBy { sn -> sn.number }.map { sn ->
                                                                            NoteDetailsDto().apply {
                                                                                this.uuid = sn.uuid
                                                                                this.name = sn.noteName
                                                                                this.note = if (sn.note != null) {
                                                                                    sn.note.toString().replace(".", ",")
                                                                                } else {
                                                                                    ""
                                                                                }
                                                                                this.number = sn.number
                                                                            }
                                                                        }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                }
                            }.sortedBy { it.name }.sortedBy { it.lastname }
                        }
                    }
                }
            }
        }
    }

    override fun submitNotes(noteDto: NoteDto, teacher: UUID): NoteDto {
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(classroomSubjects.mapNotNull { it.uuidClassroom }, LocalDateTime.now().year)
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(classroomStudents.mapNotNull { it.uuid }, classroomSubjects.mapNotNull { it.uuidSubject })

        val judgments = judgmentRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })

        val allNotes = noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }.flatMap { it.periods!! }.flatMap { it.notes!! }
        val allJudgments = noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }.flatMap { it.periods!! }.flatMap { it.notes!! }

        val toDel = (studentNotes.filterNot { sn -> allNotes.mapNotNull { it.uuid }.contains(sn.uuid) })
        val toUpdate = mutableMapOf<UUID, StudentNoteRequest>()
        val toCreate = mutableListOf<StudentNoteRequest>()

        val toDelJ = (judgments.filterNot { j -> allJudgments.mapNotNull { it.uuid }.contains(j.uuid) })
        val toUpdateJ = mutableMapOf<UUID, JudgmentRequest>()
        val toCreateJ = mutableListOf<JudgmentRequest>()

        noteDto.grades?.forEach { g ->
            g.classrooms?.forEach { c ->
                c.students?.forEach { s ->
                    val cs = classroomStudents.first { cst -> cst.uuidStudent == s.uuid && cst.uuidClassroom == c.uuid }
                    s.subjects?.forEach { u ->
                        u.periods?.forEach { p ->
                            p.notes?.forEach { n ->
                                val reqJ = JudgmentRequest().apply {
                                    this.name = p.judgment
                                    this.period = p.number
                                    this.uuidClassroomStudent = cs.uuid
                                    this.uuidSubject = u.uuid
                                    this.uuidStudent = s.uuid
                                }
                                val found = judgments.firstOrNull { ju -> ju.uuidSubject == u.uuid && ju.uuidClassroomStudent == cs.uuid && ju.period == p.number }
                                if (found != null) {
                                    toUpdateJ[found.uuid!!] = reqJ
                                } else {
                                    toCreateJ.add(reqJ)
                                }

                                val req = StudentNoteRequest().apply {
                                    this.uuidClassroomStudent = cs.uuid
                                    this.uuidSubject = u.uuid
                                    this.number = n.number
                                    this.note = if (n.note != null && n.note != "") { n.note?.replace(",", ".")?.toDouble() } else { null }
                                    this.noteName = n.name
                                    this.uuidStudent = s.uuid
                                    this.period = p.number
                                }
                                if (n.uuid != null) {
                                    toUpdate[n.uuid!!] = req
                                } else {
                                    toCreate.add(req)
                                }
                            }
                        }
                    }
                }
            }
        }
        studentNoteService.saveMultiple(toCreate)
        studentNoteService.updateMultiple(
            toUpdate.map {
                StudentNoteDto().apply {
                    this.uuid = it.key
                    this.note = it.value.note
                    this.number = it.value.number
                    this.noteName = it.value.noteName
                    this.uuidSubject = it.value.uuidSubject
                    this.uuidClassroomStudent = it.value.uuidClassroomStudent
                    this.uuidStudent = it.value.uuidStudent
                }
            }
        )
        studentNoteRepository.deleteByUuids(toDel.mapNotNull { it.uuid })
        return noteDto
    }
}
