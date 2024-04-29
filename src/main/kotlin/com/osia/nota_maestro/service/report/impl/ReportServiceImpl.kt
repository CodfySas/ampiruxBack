package com.osia.nota_maestro.service.report.impl

import com.osia.nota_maestro.dto.note.v1.AccompanimentPeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteDetailsDto
import com.osia.nota_maestro.dto.note.v1.NotePeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.note.v1.ObservationPeriodDto
import com.osia.nota_maestro.dto.note.v1.ReportStudentNote
import com.osia.nota_maestro.repository.accompanimentStudent.AccompanimentStudentRepository
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.director.DirectorRepository
import com.osia.nota_maestro.repository.directorStudent.DirectorStudentRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.teacher.TeacherRepository
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
    private val directorStudentRepository: DirectorStudentRepository,
    private val accompanimentStudentRepository: AccompanimentStudentRepository,
    private val directorRepository: DirectorRepository,
    private val teacherRepository: TeacherRepository,
    private val gradeRepository: GradeRepository,
    private val studentNoteRepository: StudentNoteRepository
) : ReportService {

    override fun getByMultipleStudent(list: List<UUID>): List<ReportStudentNote> {
        val classroomStudents = classroomStudentRepository.findAllById(list)
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        if (students.isEmpty()) {
            return emptyList()
        }
        val sortedClassroomStudents = classroomStudents.sortedBy { classroomStudent ->
            val user = students.find { it.uuid == classroomStudent.uuidStudent }
            user?.lastname
        }
        val schoolFound = schoolService.getById(students.firstOrNull()?.uuidSchool!!)
        val maxNote = schoolFound.maxNote ?: 5.0
        val minNote = schoolFound.minNote ?: 3.0
        val superior = maxNote - (maxNote / 10)
        val alto = maxNote - (maxNote / 5)

        val classroomsInYear = classroomRepository.findByUuidInAndYear(
            sortedClassroomStudents.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(classroomsInYear.mapNotNull { it.uuidGrade })
        val subjectsParents = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent == null }
        val subjectsChildren = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject }.distinct())
            .filter { it.uuidParent != null }
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            sortedClassroomStudents.mapNotNull { it.uuid },
            gradeSubjects.mapNotNull { it.uuidSubject }
        )
        val reportStudent = mutableListOf<ReportStudentNote>()
        val periods = subjectsParents.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, schoolFound.actualYear!!)
                .filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()

        val classrooms = classroomRepository.findAllById(sortedClassroomStudents.mapNotNull { it.uuidClassroom }.distinct())
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuid })

        val directorStudents = directorStudentRepository.getAllByUuidClassroomStudentIn(sortedClassroomStudents.mapNotNull { it.uuid }.distinct())
        val accompanimentStudents = accompanimentStudentRepository.getAllByUuidClassroomStudentIn(sortedClassroomStudents.mapNotNull { it.uuid }.distinct())

        val directors = directorRepository.getAllByUuidClassroomIn(sortedClassroomStudents.mapNotNull { it.uuidClassroom })
        val teachers = userRepository.findAllById(directors.mapNotNull { it.uuidTeacher })

        sortedClassroomStudents.forEach { cs ->
            val promByPeriod = mutableMapOf<String, List<Pair<Double?, Double?>>>()
            val myDirectorStudent = directorStudents.filter { ds -> ds.uuidClassroomStudent == cs.uuid }
            val myAccompanimentStudent = accompanimentStudents.filter { ds -> ds.uuidClassroomStudent == cs.uuid }
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
                                    this.judgment = ssP?.judgment ?: ""
                                    this.observation = ssP?.observation ?: ""
                                    this.def = (ssP?.def.toString().replace(".", ","))
                                    this.basic = (ssP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                    this.color = getColor(this.basic)
                                    this.recovery = (ssP?.recovery.toString().replace(".", ","))
                                    this.recoveryBasic = (ssP?.recovery?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                    val prom = promByPeriod.getOrPut("${cs.uuid}-${p.number}") { mutableListOf() }.toMutableList()
                                    prom.add(Pair(ssP?.def, ssP?.recovery))
                                    promByPeriod["${cs.uuid}-${p.number}"] = prom
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
                                            this.judgment = ssChP?.judgment ?: ""
                                            this.observation = ssChP?.observation ?: ""
                                            this.def = ((ssChP?.def?.toString()?.replace(".", ",")) ?: "")
                                            this.basic = (ssChP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                            this.color = getColor(this.basic)
                                            this.recovery = (ssChP?.recovery.toString().replace(".", ","))
                                            this.recoveryBasic = (ssChP?.recovery?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
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
                                        this.recoveryBasic = (recoveryCh?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
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
                                this.recoveryBasic = (recoveryF?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                            }
                        )
                    }
                    this.report = myNotes
                    val myDirector = directors.firstOrNull { it.uuidClassroom == cs.uuidClassroom }
                    val myTeacher = teachers.firstOrNull { it.uuid == myDirector?.uuidTeacher }
                    this.director = myTeacher?.name + ' ' + myTeacher?.lastname
                    this.position = cs.position
                    this.prom = cs.prom.toString().replace(".",",")
                    this.promBasic = cs.prom?.let { getBasic(it, superior, alto, minNote) } ?: ""
                    val myCl = classrooms.firstOrNull { it.uuid == cs.uuidClassroom }
                    val myGr = grades.firstOrNull { it.uuid == myCl?.uuidGrade }
                    this.classroom = myCl?.name + '-' + myGr?.name
                    this.observations = myDirectorStudent.map { ds ->
                        ObservationPeriodDto().apply {
                            this.period = ds.period
                            this.description = ds.description ?: ""
                        }
                    }
                    this.accompaniments = myAccompanimentStudent.map { ds ->
                        AccompanimentPeriodDto().apply {
                            this.period = ds.period
                            this.description = ds.description ?: ""
                        }
                    }
                    this.periodDef = periods.map { p->
                        NotePeriodDto().apply {
                            this.number = p.number
                            val founds = promByPeriod["${cs.uuid}-${p.number}"]
                            var sum = 0.0
                            var count = 0
                            if(schoolFound.recoveryType == "at_last"){
                                founds?.forEach {
                                    if(it.first != null){
                                        sum += it.first!!
                                        count++;
                                    }
                                }
                            }else{
                                founds?.forEach {
                                    if(it.second != null){
                                        sum += it.second!!
                                        count++;
                                    }else{
                                        if(it.first != null){
                                            sum += it.first!!
                                            count++;
                                        }
                                    }
                                }
                            }
                            if(count > 0){
                                this.def = (sum/count).toString().replace(".",",")
                                this.basic = getBasic(sum/count, superior, alto, minNote)
                            }else{
                                this.def = ""
                                this.basic = ""
                            }
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
        val myAccompanimentStudent = accompanimentStudentRepository.getAllByUuidClassroomStudent(myClassStudentActual.uuid!!)

        val myNotes = mutableListOf<NoteSubjectsDto>()
        val periods = subjectsParents.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, schoolFound.actualYear!!)
                .filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()
        val promByPeriod = mutableMapOf<String, List<Pair<Double?, Double?>>>()

        val allNotes = studentNoteRepository.findAllByUuidClassroomStudentIn(
                mutableListOf(myClassStudentActual.uuid!!)
            )
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
                        this.judgment = ssP?.judgment ?: ""
                        this.observation = ssP?.observation ?: ""
                        val myPNotes = allNotes.filter { n ->
                            n.period == ssP?.period && n.uuidClassroomStudent == myClassStudentActual.uuid &&
                                    n.uuidSubject == it.uuid
                        }
                        this.notes = myPNotes.map { n ->
                            NoteDetailsDto().apply {
                                this.note = (n.note?.toString()?.replace(".", ",") ?: "")
                                this.number = (n.number)
                                this.name = n.noteName
                            }
                        }
                        this.def = (ssP?.def.toString().replace(".", ","))
                        this.basic = (ssP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                        this.color = getColor(this.basic)
                        this.recovery = (ssP?.recovery.toString().replace(".", ","))
                        this.recoveryBasic = (ssP?.recovery?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                        val prom = promByPeriod.getOrPut("${myClassStudentActual.uuid}-${p.number}") { mutableListOf() }.toMutableList()
                        prom.add(Pair(ssP?.def, ssP?.recovery))
                        promByPeriod["${myClassStudentActual.uuid}-${p.number}"] = prom
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
                                this.judgment = ssChP?.judgment ?: ""
                                this.observation = ssChP?.observation ?: ""
                                val myPNotes = allNotes.filter { n ->
                                    n.period == ssChP?.period && n.uuidClassroomStudent == myClassStudentActual.uuid &&
                                            n.uuidSubject == ch.uuid
                                }
                                this.notes = myPNotes.map { n ->
                                    NoteDetailsDto().apply {
                                        this.note = (n.note?.toString()?.replace(".", ",") ?: "")
                                        this.number = (n.number)
                                        this.name = n.noteName
                                    }
                                }
                                this.def = ((ssChP?.def?.toString()?.replace(".", ",")) ?: "")
                                this.basic = (ssChP?.def?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                                this.color = getColor(this.basic)
                                this.recovery = (ssChP?.recovery.toString().replace(".", ","))
                                this.recoveryBasic = (ssChP?.recovery?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
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
                            this.recoveryBasic = (recoveryCh?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
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
                    this.recoveryBasic = (recoveryF?.let { it1 -> getBasic(it1, superior, alto, minNote) }) ?: ""
                }
            )
        }
        val director = myClassStudentActual.uuidClassroom?.let { directorRepository.findFirstByUuidClassroom(it).orElse(null) }
        val teacher = director?.uuidTeacher?.let { userRepository.getByUuid(it).orElseGet(null) }
        val classroom = myClassStudentActual.uuidClassroom?.let { classroomRepository.getByUuid(it).orElseGet(null) }
        val grade = classroom?.uuidGrade?.let { gradeRepository.getByUuid(it).orElse(null) }
        return ReportStudentNote().apply {
            this.report = myNotes
            this.observations = myDirectorStudent.map { d ->
                ObservationPeriodDto().apply {
                    this.period = d.period
                    this.description = d.description ?: ""
                }
            }
            this.accompaniments = myAccompanimentStudent.map { ds ->
                AccompanimentPeriodDto().apply {
                    this.period = ds.period
                    this.description = ds.description ?: ""
                }
            }
            this.name = studentFound.name
            this.lastname = studentFound.lastname
            this.director = teacher?.name + ' ' + teacher?.lastname
            this.position = myClassStudentActual.position
            this.prom = myClassStudentActual.prom.toString().replace(".",",")
            this.promBasic = myClassStudentActual.prom?.let { getBasic(it, superior, alto, minNote) } ?: ""
            this.classroom = grade?.name + '-' + classroom?.name
            this.periodDef = periods.map { p->
                NotePeriodDto().apply {
                    this.number = p.number
                    val founds = promByPeriod["${myClassStudentActual.uuid}-${p.number}"]
                    var sum = 0.0
                    var count = 0
                    if(schoolFound.recoveryType == "at_last"){
                        founds?.forEach {
                            if(it.first != null){
                                sum += it.first!!
                                count++;
                            }
                        }
                    }else{
                        founds?.forEach {
                            if(it.second != null){
                                sum += it.second!!
                                count++;
                            }else{
                                if(it.first != null){
                                    sum += it.first!!
                                    count++;
                                }
                            }
                        }
                    }
                    if(count > 0){
                        this.def = (sum/count).toString().replace(".",",")
                        this.basic = getBasic(sum/count, superior, alto, minNote)
                    }else{
                        this.def = ""
                        this.basic = ""
                    }
                }
            }
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
