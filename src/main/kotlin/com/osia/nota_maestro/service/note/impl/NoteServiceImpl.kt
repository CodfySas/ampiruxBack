package com.osia.nota_maestro.service.note.impl

import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectDto
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectMapper
import com.osia.nota_maestro.dto.note.v1.NoteDetailsDto
import com.osia.nota_maestro.dto.note.v1.NoteDto
import com.osia.nota_maestro.dto.note.v1.NotePeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteStudentDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.resources.v1.ResourceClassroomDto
import com.osia.nota_maestro.dto.resources.v1.MyAssignmentDto
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.dto.resources.v1.ResourcePeriodDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.dto.resources.v1.ResourceSubjectDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteRequest
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectMapper
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.ClassroomStudent
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.judgment.JudgmentRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.note.NoteService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.studentNote.StudentNoteService
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
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
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val studentSubjectService: StudentSubjectService,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val schoolService: SchoolService,
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val studentSubjectMapper: StudentSubjectMapper,
    private val gradeSubjectMapper: GradeSubjectMapper
) : NoteService {

    override fun getYears(schoolUUID: UUID): List<Int> {
        val periods = schoolPeriodRepository.findAllByUuidSchool(schoolUUID).sortedByDescending { it.actualYear }.mapNotNull { it.actualYear }
        return periods.distinct()
    }

    override fun getMyResources(teacher: UUID, year: Int): MyAssignmentDto {
        val teacherFound = userRepository.findById(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
        var classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = if (teacherFound.role == "teacher") {
            classroomRepository.findByUuidInAndYear(
                classroomSubjects.mapNotNull { it.uuidClassroom },
                if (year == 0) { schoolFound.actualYear!! } else { year }
            )
        } else {
            classroomRepository.findAllByUuidSchoolAndYear(schoolFound.uuid!!, year)
        }
        if (teacherFound.role != "teacher") {
            classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        }
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade }.distinct()).sortedBy { it.ordered }
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        var periods = grades.firstOrNull()?.uuidSchool?.let {
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(
                it,
                if (year == 0) { schoolFound.actualYear!! } else { year }
            )
        }
            ?.filter {
                it.init != null && it.finish != null
            }?.sortedBy { it.number } ?: mutableListOf()

        if (year == 0) {
            periods = periods.filter {
                it.init!! <= LocalDateTime.now().plusDays(1) &&
                    it.finish!!.plusDays(1) >= LocalDateTime.now()
            }
        }

        return MyAssignmentDto().apply {
            this.periods = periods.map { p ->
                ResourcePeriodDto().apply {
                    this.uuid = p.uuid
                    this.number = p.number
                    this.grades = grades.map { g ->
                        ResourceGradeDto().apply {
                            this.uuid = g.uuid
                            this.name = g.name
                            this.classrooms = classrooms.filter { c -> c.uuidGrade == g.uuid }.map { c ->
                                ResourceClassroomDto().apply {
                                    this.uuid = c.uuid
                                    this.name = c.name
                                    this.subjects =
                                        classroomSubjects.filter { cs -> cs.uuidClassroom == c.uuid }.map { cs ->
                                            ResourceSubjectDto().apply {
                                                val subject = subjects.firstOrNull { s -> s.uuid == cs.uuidSubject }
                                                this.uuid = subject?.uuid
                                                this.name = subject?.name
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getMyNotes(teacher: UUID, request: ResourceRequest): NoteDto {
        val user = userRepository.findById(teacher)
        var periods = schoolPeriodRepository.findAllByUuidSchool(user.get().uuidSchool!!)

        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(request.classroom)
        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectAndPeriod(
            classroomStudents.mapNotNull { it.uuid },
            request.subject,
            request.period
        )
        val allNotes =
            studentNoteRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid }.distinct())
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())

        val studentSubjectAll = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectAndPeriodIn(
            classroomStudents.mapNotNull { it.uuid },
            request.subject,
            periods.mapNotNull { it.number }
        )

        return NoteDto().apply {
            this.judgments = studentSubjectAll.map { it.judgment }.filter { it != "" }.distinct()
            this.students = classroomStudents.map { cs ->
                NoteStudentDto().apply {
                    val student = students.firstOrNull { it.uuid == cs.uuidStudent }
                    val studentSubject = studentSubjects.firstOrNull { it.uuidClassroomStudent == cs.uuid }
                    this.uuid = cs.uuid
                    this.name = student?.name
                    this.lastname = student?.lastname
                    this.def = studentSubject?.def?.toString()?.replace(".", ",") ?: ""
                    this.judgment = studentSubject?.judgment ?: ""
                    val myNotes = allNotes.filter {
                        it.uuidSubject == request.subject && it.period == request.period &&
                            it.uuidClassroomStudent == cs.uuid
                    }.map { n ->
                        NoteDetailsDto().apply {
                            this.note = n.note?.toString()?.replace(".", ",") ?: ""
                            this.name = n.noteName
                            this.uuid = n.uuid
                            this.number = n.number
                        }
                    }

                    this.notes = myNotes.ifEmpty {
                        mutableListOf(
                            NoteDetailsDto().apply {
                                this.number = 0
                            }
                        )
                    }
                }
            }
        }
    }

    override fun getMyNotesArchive(teacher: UUID, year: Int, type: String, request: ResourceRequest): NoteDto {
        val my = userRepository.findById(teacher)
        val schoolFound = schoolService.getById(my.get().uuidSchool!!)
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(request.classroom)
        val students0 = studentSubjectRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val classroom = classroomRepository.findById(request.classroom)
        var gradeSubjects = gradeSubjectRepository.findAllByUuidGrade(classroom.get().uuidGrade!!)
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        if (my.get().role == "teacher") {
            val cs = classroomSubjectRepository.getAllByUuidTeacher(teacher).filter { it.uuidClassroom == request.classroom }
            gradeSubjects = gradeSubjects.filter { cs.mapNotNull { cc -> cc.uuidSubject }.contains(it.uuidSubject) }
        }
        if (type == "all_subjects") {
            val subjects = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            val gradeParents = gradeSubjects.filter { gs -> subjects.filter { it.uuidParent == null }.mapNotNull { it.uuid }.contains(gs.uuidSubject) }
            return NoteDto().apply {
                this.students = classroomStudents.map { cs ->
                    NoteStudentDto().apply {
                        var sumT = 0.0
                        var notesT = 0
                        val student = students.firstOrNull { it.uuid == cs.uuidStudent }
                        val myStudentSubjects = students0.filter { it.uuidClassroomStudent == cs.uuid }
                        this.uuid = cs.uuid
                        this.name = student?.name
                        this.lastname = student?.lastname
                        this.subjects = gradeParents.map { gs ->
                            NoteSubjectsDto().apply {
                                this.name = subjects.firstOrNull { it.uuid == gs.uuidSubject }?.name
                                val ss = myStudentSubjects.filter { it.uuidSubject == gs.uuidSubject }
                                val ss0 = ss.firstOrNull { it.period == 0 }
                                val ssp = ss.filter { it.period != 0 }
                                if (schoolFound.recoveryType == "atl_last") {
                                    this.def = (ss0?.recovery?.toString()?.replace(".", ",") ?: (ss0?.def?.toString()?.replace(".", ",") ?: ""))
                                } else {
                                    var sum = 0.0
                                    var notes = 0
                                    ssp.forEach { p ->
                                        val def = p.recovery ?: p.def
                                        if (def != null) {
                                            sum += def
                                            notes ++
                                        }
                                    }
                                    if (sum > 0.0) {
                                        this.def = (sum / notes).toString().replace(".", ",")
                                        sumT += (sum / notes)
                                        notesT++
                                    } else {
                                        this.def = ""
                                    }
                                }
                            }
                        }
                        if (sumT > 0.0) {
                            this.def = (sumT / notesT).toString().replace(".", ",")
                        } else {
                            this.def = ""
                        }
                    }
                }
            }
        } else {
            val allNotes =
                studentNoteRepository.findAllByUuidClassroomStudentIn(
                    classroomStudents.mapNotNull { it.uuid }
                        .distinct()
                )
            return NoteDto().apply {
                this.students = classroomStudents.map { cs ->
                    NoteStudentDto().apply {
                        val student = students.firstOrNull { it.uuid == cs.uuidStudent }
                        val myStudentSubjects = students0.filter { it.uuidClassroomStudent == cs.uuid }
                        this.uuid = cs.uuid
                        this.name = student?.name
                        this.lastname = student?.lastname
                        val ss = myStudentSubjects.filter { it.uuidSubject == request.subject }
                        val ss0 = ss.firstOrNull { it.period == 0 }
                        val ssp = ss.filter { it.period != 0 }.sortedBy { it.period }
                        this.def = (ss0?.def?.toString()?.replace(".", ",") ?: "")
                        this.recovery = (ss0?.recovery?.toString()?.replace(".", ",") ?: "")
                        this.periods = ssp.map { p ->
                            NotePeriodDto().apply {
                                this.number = p.period
                                this.def = (p.def?.toString()?.replace(".", ",") ?: "")
                                this.recovery = (p.recovery?.toString()?.replace(".", ",") ?: "")
                                val myNotes = allNotes.filter { n ->
                                    n.period == p.period && n.uuidClassroomStudent == cs.uuid &&
                                        n.uuidSubject == request.subject
                                }
                                this.notes = myNotes.map { n ->
                                    NoteDetailsDto().apply {
                                        this.note = (n.note?.toString()?.replace(".", ",") ?: "")
                                        this.number = (n.number)
                                        this.name = n.noteName
                                    }
                                }
                                this.judgment = p.judgment
                            }
                        }
                    }
                }
            }
        }
    }

    override fun submitNotes(notesDto: List<NoteDto>, teacher: UUID): List<NoteDto> {
        val userFound = userRepository.getByUuid(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val classroom = classroomRepository.findAllById(notesDto.map { it.classroom }.distinct())
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroomIn(notesDto.map { it.classroom })
        val allNotesSaved =
            studentNoteRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(classroomStudents.mapNotNull { it.uuid }.distinct(), notesDto.map { it.subject })
        val allNotesToSave = notesDto.flatMap { it.students!! }.flatMap { it.notes!! }

        val toDel = (
            allNotesSaved.filterNot { sn -> allNotesToSave.mapNotNull { it.uuid }.contains(sn.uuid) }
                .filter { sn -> notesDto.map { it.period }.contains(sn.period) }
            )
        val toUpdate = mutableMapOf<UUID, StudentNoteRequest>()
        val toCreate = mutableListOf<StudentNoteRequest>()
        val judgmentsToSave = mutableListOf<StudentSubjectDto>()

        notesDto.forEach { nDto ->
            nDto.students?.forEach { s ->
                judgmentsToSave.add(
                    StudentSubjectDto().apply {
                        this.uuidClassroomStudent = s.uuid
                        this.uuidSubject = nDto.subject
                        this.period = nDto.period
                        this.judgment = s.judgment
                    }
                )
                s.notes?.forEach { n ->
                    val req = StudentNoteRequest().apply {
                        this.uuidClassroomStudent = s.uuid
                        this.uuidSubject = nDto.subject
                        this.number = n.number
                        this.note = if (n.note != null && n.note != "") {
                            n.note?.replace(",", ".")?.toDouble()
                        } else {
                            null
                        }
                        this.noteName = n.name
                        this.period = nDto.period
                    }
                    if (n.uuid != null) {
                        toUpdate[n.uuid!!] = req
                    } else {
                        toCreate.add(req)
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
        setNotes(classroom, userFound.uuidSchool!!, judgmentsToSave)
        return notesDto
    }

    @Async
    override fun setNotes(classrooms: List<Classroom>, schoolUUID: UUID, judgmentsSubmited: List<StudentSubjectDto>) {
        val school = schoolService.getById(schoolUUID)
        val schoolPeriods = schoolPeriodRepository.findAllByUuidSchoolAndActualYear(schoolUUID, school.actualYear!!)
        val grades = if (classrooms.isEmpty()) {
            gradeRepository.findAllByUuidSchool(schoolUUID)
        } else {
            gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade }.distinct())
        }
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(grades.mapNotNull { it.uuid })
        val subjects = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())

        val gradeSubjectsChildren = gradeSubjects.filter { gs ->
            subjects.filter { sx -> sx.uuidParent != null }.mapNotNull { sx -> sx.uuid }.contains(gs.uuidSubject)
        }
        val gradeSubjectsParents = gradeSubjects.filter { gs ->
            subjects.filter { sx -> sx.uuidParent == null }
                .mapNotNull { sx -> sx.uuid }.contains(gs.uuidSubject)
        }.map(gradeSubjectMapper::toDto)

        gradeSubjectsParents.forEach { gs ->
            gs.children = gradeSubjectsChildren.filter { gc ->
                subjects.filter { sx -> sx.uuidParent == gs.uuidSubject }
                    .mapNotNull { sx -> sx.uuid }.contains(gc.uuidSubject)
            }.map(gradeSubjectMapper::toDto)
        }

        val classroomsToEdit = classrooms.ifEmpty {
            classroomRepository.findAllByUuidGradeIn(grades.mapNotNull { it.uuid })
        }
        val classroomStudents =
            classroomStudentRepository.getAllByUuidClassroomIn(classroomsToEdit.mapNotNull { it.uuid })

        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            gradeSubjects.mapNotNull { it.uuidSubject }
        )

        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            gradeSubjects.mapNotNull { it.uuidSubject }
        )

        val ssToUpdate = mutableListOf<StudentSubjectDto>()
        val ssToCreate = mutableListOf<StudentSubjectRequest>()

        classroomsToEdit.forEach { c ->
            // by class
            val myStudents = classroomStudents.filter { cs -> cs.uuidClassroom == c.uuid }
            myStudents.forEach { cs ->
                // by student in class
                gradeSubjectsParents.forEach { gs ->
                    // subjects by student
                    val myStudentSubjects = studentSubjects.filter { ss ->
                        ss.uuidSubject == gs.uuidSubject &&
                            ss.uuidClassroomStudent == cs.uuid
                    }
                    val ss0 = myStudentSubjects.firstOrNull { ss -> ss.period == 0 }
                    val ss0Children = mutableMapOf<UUID, Pair<Double, Int>>()

                    var def0Final: Double? = null
                    var sumFinal = 0.0
                    var notesFinal = 0
                    schoolPeriods.forEach { p ->
                        var defFinal: Double? = null
                        val myNotes = studentNotes.filter { sn ->
                            sn.period == p.number && sn.uuidClassroomStudent == cs.uuid &&
                                sn.uuidSubject == gs.uuidSubject && sn.note != null
                        }
                        if (gs.children.isNullOrEmpty()) {
                            if (myNotes.isNotEmpty()) {
                                defFinal = (myNotes.sumOf { n -> n.note!! } / myNotes.size)
                                sumFinal += defFinal
                                notesFinal++
                            }
                        } else {
                            var sumInP = 0.0
                            var countInP = 0
                            gs.children?.forEach { ch ->
                                var defPerChildren: Double? = null
                                val childrenNoteInPeriod = studentNotes.filter { sn ->
                                    sn.period == p.number &&
                                        sn.uuidClassroomStudent == cs.uuid && sn.uuidSubject == ch.uuidSubject && sn.note != null
                                }
                                val sch = studentSubjects.firstOrNull { ss ->
                                    ss.uuidSubject == ch.uuidSubject && ss.uuidClassroomStudent == cs.uuid &&
                                        ss.period == p.number
                                }
                                if (childrenNoteInPeriod.isNotEmpty()) {
                                    defPerChildren =
                                        (childrenNoteInPeriod.sumOf { n -> n.note!! } / childrenNoteInPeriod.size)

                                    sumInP += defPerChildren
                                    countInP++
                                }

                                if (sch == null) {
                                    ssToCreate.add(newR(cs, p.number, schoolUUID, defPerChildren, ch))
                                } else {
                                    ssToUpdate.add(
                                        studentSubjectMapper.toDto(sch).apply {
                                            this.def = defPerChildren
                                        }
                                    )
                                }
                                if (defPerChildren != null) {
                                    var value = ss0Children.getOrPut(ch.uuidSubject!!) { Pair(0.0, 0) }
                                    value = Pair(value.first + defPerChildren, value.second + 1)
                                    ss0Children[ch.uuidSubject!!] = value
                                }
                            }
                            if (sumInP > 0.0) {
                                defFinal = (sumInP / countInP)
                                sumFinal += defFinal
                                notesFinal++
                            }
                        }

                        val ss = myStudentSubjects.firstOrNull { ss -> ss.period == p.number }
                        if (ss == null) {
                            ssToCreate.add(newR(cs, p.number, schoolUUID, defFinal, gs))
                        } else {
                            ssToUpdate.add(
                                studentSubjectMapper.toDto(ss).apply {
                                    this.def = defFinal
                                }
                            )
                        }
                    }
                    gs.children?.forEach { chx ->
                        var defCh0: Double? = null
                        val children0 = studentSubjects.firstOrNull { ss ->
                            ss.uuidSubject == chx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == 0
                        }
                        val childrenNote = ss0Children[chx.uuidSubject]
                        if (childrenNote != null && childrenNote.first > 0.0) {
                            defCh0 = childrenNote.first / childrenNote.second
                        }
                        if (children0 == null) {
                            ssToCreate.add(newR(cs, 0, schoolUUID, defCh0, chx))
                        } else {
                            ssToUpdate.add(
                                    studentSubjectMapper.toDto(children0).apply {
                                        this.def = defCh0
                                    }
                            )
                        }
                    }
                    if (sumFinal > 0.0) {
                        def0Final = (sumFinal / notesFinal)
                    }
                    if (ss0 == null) {
                        ssToCreate.add(newR(cs, 0, schoolUUID, def0Final, gs))
                    } else {
                        ssToUpdate.add(
                            studentSubjectMapper.toDto(ss0).apply {
                                this.def = def0Final
                            }
                        )
                    }
                }
            }
        }
        val ssTd = studentSubjects.filterNot { ss -> ssToUpdate.mapNotNull { it.uuid }.contains(ss.uuid) }

        if (judgmentsSubmited.isNotEmpty()) {
            ssToCreate.forEach { ss ->
                val find = judgmentsSubmited.firstOrNull { js ->
                    js.uuidSubject == ss.uuidSubject &&
                        js.uuidClassroomStudent == ss.uuidClassroomStudent && js.period == ss.period
                }
                if (find != null) {
                    ss.judgment = find.judgment
                }
            }

            ssToUpdate.forEach { ss ->
                val find = judgmentsSubmited.firstOrNull { js ->
                    js.uuidSubject == ss.uuidSubject &&
                        js.uuidClassroomStudent == ss.uuidClassroomStudent && js.period == ss.period
                }
                if (find != null) {
                    ss.judgment = find.judgment
                }
            }
        }

        studentSubjectService.saveMultiple(ssToCreate)
        studentSubjectService.updateMultiple(ssToUpdate)

        studentSubjectRepository.deleteByUuids(ssTd.mapNotNull { it.uuid })
    }

    private fun newR(
        cs: ClassroomStudent,
        period: Int?,
        schoolUUID: UUID,
        defPerChildren: Double?,
        ch: GradeSubjectDto
    ): StudentSubjectRequest {
        return StudentSubjectRequest().apply {
            this.uuidStudent = cs.uuidStudent
            this.period = period
            this.uuidSchool = schoolUUID
            this.def = defPerChildren
            this.recovery = null
            this.uuidClassroomStudent = cs.uuid
            this.uuidSubject = ch.uuidSubject
        }
    }
}
