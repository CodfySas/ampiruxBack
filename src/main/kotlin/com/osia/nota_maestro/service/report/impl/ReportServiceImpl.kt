package com.osia.nota_maestro.service.report.impl

import com.osia.nota_maestro.dto.note.v1.NotePeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.note.v1.ObservationPeriodDto
import com.osia.nota_maestro.dto.note.v1.ReportStudentNote
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.directorStudent.DirectorStudentRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.report.ReportService
import com.osia.nota_maestro.service.school.SchoolService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service("report.crud_service")
@Transactional
class ReportServiceImpl(
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val userRepository: UserRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val subjectRepository: SubjectRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val schoolService: SchoolService,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val directorStudentRepository: DirectorStudentRepository
) : ReportService {

    override fun getByMultipleStudent(list: List<UUID>): List<ReportStudentNote> {
        val classroomStudents = classroomStudentRepository.findAllById(list)
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        if (students.isEmpty()) {
            return emptyList()
        }
        val schoolFound = schoolService.getById(students.firstOrNull()?.uuidSchool!!)
        val maxNote = schoolFound.maxNote ?: 5.0
        val minNote = schoolFound.minNote ?: 3.0
        val superior = maxNote - (maxNote / 10)
        val alto = maxNote - (maxNote / 5)

        val classroomsInYear = classroomRepository.findByUuidInAndYear(
            classroomStudents.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(classroomsInYear.mapNotNull { it.uuidGrade })
        val subjectsParents = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent == null }
        val subjectsChildren = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent != null }
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            gradeSubjects.mapNotNull { it.uuidSubject }
        )
        val reportStudent = mutableListOf<ReportStudentNote>()
        val periods = subjectsParents.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, schoolFound.actualYear!!)
                .filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()

        val directorStudents = directorStudentRepository.getAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid }.distinct())

        classroomStudents.forEach { cs ->
            val myDirectorStudent = directorStudents.filter { ds -> ds.uuidClassroomStudent == cs.uuid }
            val user = students.firstOrNull { s -> s.uuid == cs.uuidStudent }
            reportStudent.add(
                ReportStudentNote().apply {
                    this.name = user?.name
                    this.lastname = user?.lastname
                    val myNotes = mutableListOf<NoteSubjectsDto>()
                    subjectsParents.forEach {
                        val myNotePeriods = mutableListOf<NotePeriodDto>()
                        var recoveryF: Double? = null
                        var defF: String = ""
                        val studentSubject0Found = studentSubject.firstOrNull { ss -> ss.uuidClassroomStudent == cs.uuid && ss.uuidSubject == it.uuid && ss.period == 0 }
                        if (studentSubject0Found?.def != null) {
                            defF = (studentSubject0Found.def?.toString()?.replace(".", ",") ?: "")
                        }
                        if (studentSubject0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                            recoveryF = studentSubject0Found.recovery
                        }
                        periods.sortedBy { it.number }.forEach { p ->
                            val ssP = studentSubject.firstOrNull { ss -> ss.uuidClassroomStudent == cs.uuid && ss.uuidSubject == it.uuid && ss.period == p.number }
                            myNotePeriods.add(
                                NotePeriodDto().apply {
                                    this.number = p.number
                                    this.defi = (ssP?.def)
                                    this.def = (ssP?.def.toString().replace(".", ","))
                                    this.basic = (ssP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                    this.color = getColor(this.basic)
                                }
                            )
                        }

                        val children = mutableListOf<NoteSubjectsDto>()
                        val myChildren = subjectsChildren.filter { sc -> sc.uuidParent == it.uuid }
                        if (myChildren.isNotEmpty()) {
                            myChildren.forEach { ch ->
                                val myChPeriods = mutableListOf<NotePeriodDto>()
                                var recoveryCh: Double? = null
                                var defCh: String = ""
                                val studentSubjectCh0Found = studentSubject.firstOrNull { ss -> ss.uuidClassroomStudent == cs.uuid && ss.uuidSubject == ch.uuid && ss.period == 0 }
                                if (studentSubjectCh0Found?.def != null) {
                                    defCh = (studentSubjectCh0Found.def?.toString()?.replace(".", ",") ?: "")
                                }
                                if (studentSubjectCh0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                                    recoveryCh = studentSubjectCh0Found.recovery
                                }
                                periods.sortedBy { it.number }.forEach { p ->
                                    val ssChP = studentSubject.firstOrNull { ss -> ss.uuidClassroomStudent == cs.uuid && ss.uuidSubject == ch.uuid && ss.period == p.number }
                                    myChPeriods.add(
                                        NotePeriodDto().apply {
                                            this.number = p.number
                                            this.def = ((ssChP?.def?.toString()?.replace(".", ",")) ?: "")
                                            this.basic = (ssChP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                            this.color = getColor(this.basic)
                                        }
                                    )
                                }
                                children.add(
                                    NoteSubjectsDto().apply {
                                        this.name = ch.name
                                        this.def = defCh
                                        this.periods = myChPeriods
                                        this.recovery = recoveryCh.toString()
                                        this.basic = (studentSubjectCh0Found?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                        this.color = getColor(this.basic)
                                    }
                                )
                            }
                        }
                        myNotes.add(
                            NoteSubjectsDto().apply {
                                this.name = it.name
                                this.def = defF
                                this.children = children
                                this.periods = myNotePeriods
                                this.recovery = recoveryF.toString()
                                this.basic = (studentSubject0Found?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                this.color = getColor(this.basic)
                            }
                        )
                    }
                    this.report = myNotes
                    this.observations = myDirectorStudent.map { ds ->
                        ObservationPeriodDto().apply {
                            this.period = ds.period
                            this.description = ds.description ?: ""
                        }
                    }
                }
            )
        }
        return reportStudent
    }

    override fun getByStudent(student: UUID): ReportStudentNote {
        val studentFound = userRepository.findById(student).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(studentFound.uuidSchool!!)
        val maxNote = schoolFound.maxNote ?: 5.0
        val minNote = schoolFound.minNote ?: 3.0
        val superior = maxNote - (maxNote / 10)
        val alto = maxNote - (maxNote / 5)
        val classroomStudentsAll = classroomStudentRepository.findAllByUuidStudent(student)
        val classroomsInYear = classroomRepository.findByUuidInAndYear(
            classroomStudentsAll.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        val myClassStudentActual =
            classroomStudentsAll.firstOrNull { classroomsInYear.mapNotNull { c -> c.uuid }.contains(it.uuidClassroom) }
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(classroomsInYear.mapNotNull { it.uuidGrade })
        val subjectsParents = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent == null }
        val subjectsChildren = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent != null }
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentAndUuidSubjectIn(
            myClassStudentActual?.uuid!!,
            gradeSubjects.mapNotNull { it.uuidSubject }
        )
        val myDirectorStudent = directorStudentRepository.getAllByUuidClassroomStudent(myClassStudentActual.uuid!!)
        val myNotes = mutableListOf<NoteSubjectsDto>()
        val periods = subjectsParents.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, schoolFound.actualYear!!)
                .filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()
        subjectsParents.forEach {
            val myNotePeriods = mutableListOf<NotePeriodDto>()
            var recoveryF: Double? = null
            var defF: String = ""
            val studentSubject0Found = studentSubject.firstOrNull { ss -> ss.uuidSubject == it.uuid && ss.period == 0 }
            if (studentSubject0Found?.def != null) {
                defF = (studentSubject0Found.def?.toString()?.replace(".", ",") ?: "")
            }
            if (studentSubject0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                recoveryF = studentSubject0Found.recovery
            }
            periods.sortedBy { it.number }.forEach { p ->
                val ssP = studentSubject.firstOrNull { ss -> ss.uuidSubject == it.uuid && ss.period == p.number }
                myNotePeriods.add(
                    NotePeriodDto().apply {
                        this.number = p.number
                        this.defi = (ssP?.def)
                        this.def = (ssP?.def.toString().replace(".", ","))
                        this.basic = (ssP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                        this.color = getColor(this.basic)
                    }
                )
            }

            val children = mutableListOf<NoteSubjectsDto>()
            val myChildren = subjectsChildren.filter { sc -> sc.uuidParent == it.uuid }
            if (myChildren.isNotEmpty()) {
                myChildren.forEach { ch ->
                    val myChPeriods = mutableListOf<NotePeriodDto>()
                    var recoveryCh: Double? = null
                    var defCh: String = ""
                    val studentSubjectCh0Found = studentSubject.firstOrNull { ss -> ss.uuidSubject == ch.uuid && ss.period == 0 }
                    if (studentSubjectCh0Found?.def != null) {
                        defCh = (studentSubjectCh0Found.def?.toString()?.replace(".", ",") ?: "")
                    }
                    if (studentSubjectCh0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                        recoveryCh = studentSubjectCh0Found.recovery
                    }
                    periods.sortedBy { it.number }.forEach { p ->
                        val ssChP = studentSubject.firstOrNull { ss -> ss.uuidSubject == ch.uuid && ss.period == p.number }
                        myChPeriods.add(
                            NotePeriodDto().apply {
                                this.number = p.number
                                this.def = ((ssChP?.def?.toString()?.replace(".", ",")) ?: "")
                                this.basic = (ssChP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                this.color = getColor(this.basic)
                            }
                        )
                    }
                    children.add(
                        NoteSubjectsDto().apply {
                            this.name = ch.name
                            this.def = defCh
                            this.periods = myChPeriods
                            this.recovery = recoveryCh.toString()
                            this.basic = (studentSubjectCh0Found?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                            this.color = getColor(this.basic)
                        }
                    )
                }
            }
            myNotes.add(
                NoteSubjectsDto().apply {
                    this.name = it.name
                    this.def = defF
                    this.children = children
                    this.periods = myNotePeriods
                    this.recovery = recoveryF.toString()
                    this.basic = (studentSubject0Found?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                    this.color = getColor(this.basic)
                }
            )
        }
        return ReportStudentNote().apply {
            this.report = myNotes
            this.observations = myDirectorStudent.map { d ->
                ObservationPeriodDto().apply {
                    this.period = d.period
                    this.description = d.description ?: ""
                }
            }
            this.name = ""
            this.lastname = ""
        }
    }

    fun getBasic(number: Double, superior: Double, alto: Double, basic: Double): String {
        return if (number > superior) {
            "Superior"
        } else {
            if (number >= alto) {
                "Alto"
            } else {
                if (number >= basic) {
                    "Basico"
                } else {
                    if (number >= 0.1) {
                        "Bajo"
                    } else {
                        ""
                    }
                }
            }
        }
    }

    fun getColor(basic: String): String {
        return when (basic) {
            "Superior" -> { "green" }
            "Bajo" -> { "red" }
            else -> { "" }
        }
    }
}
