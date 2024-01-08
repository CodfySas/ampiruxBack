package com.osia.nota_maestro.service.note.impl

import com.osia.nota_maestro.dto.judgment.v1.JudgmentDto
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
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.judgment.JudgmentRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.judgment.JudgmentService
import com.osia.nota_maestro.service.note.NoteService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.studentNote.StudentNoteService
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
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
    private val judgmentService: JudgmentService,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val studentSubjectService: StudentSubjectService,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val schoolService: SchoolService
) : NoteService {

    override fun getMyNotes(teacher: UUID): NoteDto {
        val teacherFound = userRepository.findById(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(
            classroomSubjects.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        return returnNotes(classrooms, classroomSubjects, false, schoolFound.actualYear!!, mutableListOf())
    }

    override fun getMyNotesArchive(teacher: UUID, year: Int, role: String): NoteDto {
        val myUser = userRepository.findById(teacher)
        var classes = listOf<Classroom>()
        var allYears = listOf<Int>()
        val classroomSubjects = if (role == "teacher") {
            val cs = classroomSubjectRepository.getAllByUuidTeacher(teacher)
            allYears = classroomRepository.findAllById(cs.mapNotNull { it.uuidClassroom }).mapNotNull { it.year }.distinct()
            classes = classroomRepository.findByUuidInAndYear(cs.mapNotNull { it.uuidClassroom }, year)
            cs
        } else {
            val grades = gradeRepository.findAllByUuidSchool(myUser.get().uuidSchool!!)
            classes = classroomRepository.findAllByUuidGradeInAndYear(grades.mapNotNull { it.uuid }, year)
            allYears = classroomRepository.findAllByUuidGradeIn(grades.mapNotNull { it.uuid }).mapNotNull { it.year }.distinct()
            classroomSubjectRepository.getAllByUuidClassroomIn(classes.mapNotNull { it.uuid })
        }
        return returnNotes(classes, classroomSubjects, true, year, allYears)
    }

    private fun returnNotes(
        classrooms: List<Classroom>,
        classroomSubjects: List<ClassroomSubject>,
        includeAllPeriods: Boolean,
        year: Int,
        allYears: List<Int>
    ): NoteDto {
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })
        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }
        )
        val studentNotes =
            studentNoteRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        val judgments = judgmentRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val periods = if (!includeAllPeriods) {
            grades.firstOrNull()?.uuidSchool?.let { schoolPeriodRepository.findAllByUuidSchoolAndActualYear(it, year) }?.filter {
                it.init != null && it.finish != null && it.init!! <= LocalDateTime.now()
                    .plusDays(1) && it.finish!!.plusDays(1) >= LocalDateTime.now()
            }?.sortedBy { it.number } ?: mutableListOf()
        } else {
            grades.firstOrNull()?.uuidSchool?.let { schoolPeriodRepository.findAllByUuidSchoolAndActualYear(it, year) }
                ?.filter { it.init != null && it.finish != null }?.sortedBy { it.number } ?: mutableListOf()
        }

        return NoteDto().apply {
            this.years = allYears
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
                                    this.code = student.code
                                    this.subjects =
                                        classroomSubjects.filter { cx -> cx.uuidClassroom == cs.uuidClassroom }
                                            .map { cx ->
                                                NoteSubjectsDto().apply {
                                                    this.uuid = cx.uuidSubject
                                                    this.name = subjects.first { it.uuid == cx.uuidSubject }.name
                                                    val studentSubject0Found = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == 0 }
                                                    this.def = studentSubject0Found?.def.toString().replace(".", ",")
                                                    this.recovery = studentSubject0Found?.recovery
                                                    this.uuidStudentSubject = studentSubject0Found?.uuid
                                                    this.periods = periods.map { p ->
                                                        val maxNote = studentNoteRepository.getNoteMAx(
                                                            classroomStudents.mapNotNull { it.uuid },
                                                            p.number!!,
                                                            cx.uuidSubject!!
                                                        ) ?: 1
                                                        NotePeriodDto().apply {
                                                            val judgmentFound = judgments.firstOrNull { j -> j.uuidSubject == cx.uuidSubject && j.uuidClassroomStudent == cs.uuid && j.period == p.number }
                                                            this.judgment = judgmentFound?.name ?: ""
                                                            this.uuidJudgment = judgmentFound?.uuid

                                                            val studentSubjectFound = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == p.number }
                                                            this.defi = studentSubjectFound?.def
                                                            this.recovery = studentSubjectFound?.recovery
                                                            this.uuidStudentSubject = studentSubjectFound?.uuid
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
        val userFound = userRepository.getByUuid(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(userFound.uuidSchool!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(
            classroomSubjects.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }
        )
        val studentNotes =
            studentNoteRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
                classroomStudents.mapNotNull { it.uuid },
                classroomSubjects.mapNotNull { it.uuidSubject }
            )

        val judgments = judgmentRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })

        val submitPeriods =
            noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
                .flatMap { it.periods!! }.map { it.number!! }
        val allNotes = noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
            .flatMap { it.periods!! }.flatMap { it.notes!! }
        val allJudgments =
            noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
                .flatMap { it.periods!! }
        val allStudentSubjects = noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
            .flatMap { it.periods!! }.toMutableList()

        val studentSubject0 = noteDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }.map { NotePeriodDto().apply {
            this.number = 0
            this.uuidStudentSubject = it.uuidStudentSubject
            this.defi = it.def?.replace(",",".")?.toDoubleOrNull()
            this.recovery = it.recovery
        } }

        allStudentSubjects+=studentSubject0

        val toDel = (
            studentNotes.filterNot { sn -> allNotes.mapNotNull { it.uuid }.contains(sn.uuid) }
                .filter { submitPeriods.contains(it.period) }
            )
        val toUpdate = mutableMapOf<UUID, StudentNoteRequest>()
        val toCreate = mutableListOf<StudentNoteRequest>()

        val toDelJ = (judgments.filterNot { j -> allJudgments.map { it.uuidJudgment }.contains(j.uuid) })
            .filter { submitPeriods.contains(it.period) }
        val toUpdateJ = mutableMapOf<UUID, JudgmentRequest>()
        val toCreateJ = mutableListOf<JudgmentRequest>()

        val toDelSS = (studentSubjects.filterNot { ss -> allStudentSubjects.map { it.uuidStudentSubject }.contains(ss.uuid) })
            .filter { submitPeriods.contains(it.period) }
        val toUpdateSS = mutableMapOf<UUID, StudentSubjectRequest>()
        val toCreateSS = mutableListOf<StudentSubjectRequest>()

        noteDto.grades?.forEach { g ->
            g.classrooms?.forEach { c ->
                c.students?.forEach { s ->
                    val cs = classroomStudents.first { cst -> cst.uuidStudent == s.uuid && cst.uuidClassroom == c.uuid }
                    s.subjects?.forEach { u ->
                        val newDef = if (u.def == null || u.def == "" || u.def == "null") {
                            null
                        } else {
                            u.def?.replace(",", ".")?.toDouble()
                        }
                        val reqSS0 = StudentSubjectRequest().apply {
                            this.uuidClassroomStudent = cs.uuid
                            this.uuidSchool = userFound.uuidSchool
                            this.uuidSubject = u.uuid
                            this.uuidStudent = s.uuid
                            this.period = 0
                            this.def = newDef
                        }
                        if (u.uuidStudentSubject != null) {
                            toUpdateSS[u.uuidStudentSubject!!] = reqSS0
                        } else {
                            toCreateSS.add(reqSS0)
                        }
                        u.periods?.forEach { p ->
                            val reqSS = StudentSubjectRequest().apply {
                                this.uuidClassroomStudent = cs.uuid
                                this.uuidSchool = userFound.uuidSchool
                                this.uuidSubject = u.uuid
                                this.uuidStudent = s.uuid
                                this.period = p.number
                                this.def = p.defi
                                this.recovery = p.recovery
                            }
                            if (p.uuidStudentSubject != null) {
                                toUpdateSS[p.uuidStudentSubject!!] = reqSS
                            } else {
                                toCreateSS.add(reqSS)
                            }

                            val reqJ = JudgmentRequest().apply {
                                this.name = p.judgment
                                this.period = p.number
                                this.uuidClassroomStudent = cs.uuid
                                this.uuidSubject = u.uuid
                                this.uuidStudent = s.uuid
                            }
                            if (p.uuidJudgment != null) {
                                toUpdateJ[p.uuidJudgment!!] = reqJ
                            } else {
                                toCreateJ.add(reqJ)
                            }
                            p.notes?.forEach { n ->
                                val req = StudentNoteRequest().apply {
                                    this.uuidClassroomStudent = cs.uuid
                                    this.uuidSubject = u.uuid
                                    this.number = n.number
                                    this.note = if (n.note != null && n.note != "") {
                                        n.note?.replace(",", ".")?.toDouble()
                                    } else {
                                        null
                                    }
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

        judgmentService.saveMultiple(toCreateJ)
        judgmentRepository.deleteByUuids(toDelJ.mapNotNull { it.uuid })
        judgmentService.updateMultiple(
            toUpdateJ.map {
                JudgmentDto().apply {
                    this.uuid = it.key
                    this.name = it.value.name
                    this.period = it.value.period
                    this.uuidStudent = it.value.uuidStudent
                    this.uuidSubject = it.value.uuidSubject
                    this.uuidClassroomStudent = it.value.uuidClassroomStudent
                }
            }
        )

        studentSubjectService.saveMultiple(toCreateSS)
        studentSubjectRepository.deleteByUuids(toDelSS.mapNotNull { it.uuid })
        studentSubjectService.updateMultiple(
            toUpdateSS.map {
                StudentSubjectDto().apply {
                    this.uuid = it.key
                    this.period = it.value.period
                    this.uuidStudent = it.value.uuidStudent
                    this.uuidSubject = it.value.uuidSubject
                    this.uuidClassroomStudent = it.value.uuidClassroomStudent
                    this.def = it.value.def
                    this.recovery = it.value.recovery
                    this.uuidSchool = it.value.uuidSchool
                }
            }
        )

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
