package com.osia.nota_maestro.service.home.impl

import com.osia.nota_maestro.dto.classroom.v1.ClassroomRequest
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentRequest
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectRequest
import com.osia.nota_maestro.dto.home.v1.ChartDto
import com.osia.nota_maestro.dto.home.v1.ChartSeriesDto
import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import com.osia.nota_maestro.dto.note.v1.NotePeriodDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto
import com.osia.nota_maestro.dto.school.v1.SchoolRequest
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodRequest
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroom.ClassroomService
import com.osia.nota_maestro.service.classroomStudent.ClassroomStudentService
import com.osia.nota_maestro.service.classroomSubject.ClassroomSubjectService
import com.osia.nota_maestro.service.home.HomeService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.schoolPeriod.SchoolPeriodService
import com.osia.nota_maestro.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.text.DecimalFormat
import java.util.UUID

@Service("home.crud_service")
@Transactional
class HomeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val userRepository: UserRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomService: ClassroomService,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val studentNoteRepository: StudentNoteRepository,
    private val subjectRepository: SubjectRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val schoolService: SchoolService,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val classroomSubjectService: ClassroomSubjectService,
    private val schoolPeriodService: SchoolPeriodService,
    private val classroomStudentService: ClassroomStudentService,
    private val userService: UserService
) : HomeService {

    override fun getByAdmin(school: UUID): HomeAdminDto {
        val schoolFound = schoolService.getById(school)
        val losesByStudent = mutableListOf<ChartDto>()
        val gradesProm = mutableListOf<ChartDto>()
        val gradesAll = gradeRepository.findAllByUuidSchool(school)
        val classrooms =
            classroomRepository.findAllByUuidGradeInAndYear(gradesAll.mapNotNull { it.uuid }, schoolFound.actualYear!!)
        val periods = schoolPeriodRepository.findAllByUuidSchoolAndActualYear(school, schoolFound.actualYear!!)
            .sortedBy { it.number }
        val studentClass = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentIn(studentClass.mapNotNull { it.uuid })
        val classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            studentClass.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }
        )
        val teachers = userRepository.findAllByRoleAndUuidSchool("teacher", school)

        gradesAll.forEach { g ->
            var promFinal = 0.0
            var sumFinal = 0.0
            var numbsF = 0
            val gradeClasses = classrooms.filter { c -> c.uuidGrade == g.uuid }
            gradeClasses.forEach { c ->
                val mySubjects = classroomSubjects.filter { it.uuidClassroom == c.uuid }
                val myStudentInClass = studentClass.filter { it.uuidClassroom == c.uuid }
                val promByClass: Double?
                var sumByClass = 0.0
                var notesByClass = 0
                periods.forEach { p ->
                    val promByPeriod: Double?
                    var sumByPeriod = 0.0
                    var notesByPeriod = 0
                    myStudentInClass.forEach { s ->
                        val myNotes = studentNotes.filter { it.uuidClassroomStudent == s.uuid && p.number == it.period }
                        var promByStudent: Double? = null
                        var sumByStudent = 0.0
                        var notesByStudent = 0
                        mySubjects.forEach { sx ->
                            val studentSubject0Found =
                                studentSubjects.firstOrNull { ss -> ss.uuidSubject == sx.uuidSubject && ss.uuidClassroomStudent == s.uuid && ss.period == 0 }
                            if (studentSubject0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                                sumByStudent += studentSubject0Found.recovery!!
                                notesByStudent++
                            } else {
                                val studentSubjectFound =
                                    studentSubjects.firstOrNull { ss -> ss.uuidSubject == sx.uuidSubject && ss.uuidClassroomStudent == s.uuid && ss.period == p.number }
                                val notesInSubjectPerPersonInPeriod =
                                    myNotes.filter { mn -> mn.uuidSubject == sx.uuidSubject }
                                var promBySubject: Double? = null
                                var sumBySubject = 0.0
                                var notesInSubject = 0
                                notesInSubjectPerPersonInPeriod.forEach { n ->
                                    if (n.note != null) {
                                        sumBySubject += n.note!!
                                        notesInSubject++
                                    }
                                }
                                if (sumBySubject != 0.0) {
                                    promBySubject =
                                        if (studentSubjectFound?.recovery != null && schoolFound.recoveryType != "at_last") {
                                            studentSubjectFound.recovery!!
                                        } else {
                                            sumBySubject / notesInSubject
                                        }
                                    sumByStudent += promBySubject
                                    notesByStudent++
                                }
                            }
                        }
                        if (sumByStudent != 0.0) {
                            promByStudent = sumByStudent / notesByStudent
                            sumByPeriod += promByStudent
                            notesByPeriod++
                            losesByStudent.add(
                                ChartDto().apply {
                                    this.name = s.uuidStudent.toString()
                                    this.value = sumByPeriod
                                }
                            )
                        }
                    }
                    if (sumByPeriod != 0.0) {
                        promByPeriod = sumByPeriod / notesByPeriod
                        sumByClass += promByPeriod
                        notesByClass++
                    }
                }
                if (sumByClass != 0.0) {
                    promByClass = sumByClass / notesByClass
                    sumFinal += promByClass
                    numbsF++
                }
            }
            if (sumFinal != 0.0) {
                promFinal = sumFinal / numbsF
            }
            gradesProm.add(
                ChartDto().apply {
                    this.name = g.name ?: ""
                    this.value = promFinal
                }
            )
        }

        val users = userRepository.findAll()
        val chartDto = mutableListOf<ChartDto>()
        val grades = gradeRepository.findAllById(users.mapNotNull { it.actualGrade }.distinct())

        grades.forEach {
            chartDto.add(
                (
                    ChartDto().apply {
                        this.name = it.name.toString()
                        this.value = users.filter { u -> u.actualGrade == it.uuid }.size.toDouble()
                    }
                    )
            )
        }
        val horizontal = mutableListOf<ChartDto>()
        losesByStudent.groupBy { it.name }.forEach { (k, v) ->
            var sum = 0.0
            var notes = 0
            v.forEach {
                if (it.value != null) {
                    sum += it.value ?: 0.0
                    notes++
                }
            }
            if (sum != 0.0) {
                val prom = sum / notes
                if (prom < schoolFound.minNote!!) {
                    horizontal.add(
                        ChartDto().apply {
                            val userFound = users.firstOrNull { u -> u.uuid == UUID.fromString(k) }
                            this.name = userFound?.name + " " + userFound?.lastname
                            this.value = 0.0
                        }
                    )
                }
            }
        }
        return HomeAdminDto().apply {
            this.studentsByGrade = chartDto
            this.promPerGrade = gradesProm
        }
    }

    override fun getByTeacher(teacher: UUID): HomeAdminDto {
        val teacherFound = userRepository.getByUuid(teacher).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
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
            studentNoteRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
        val byClass = classroomStudents.groupBy { it.uuidClassroom }
        val periods = studentNotes.sortedBy { it.period }.groupBy { it.period }
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })

        var valueSuperior = 0.0
        var valueAlto = 0.0
        var valueBasico = 0.0
        var valueBajo = 0.0
        var valueNotDefined = 0.0
        val perStudent = mutableMapOf<UUID, List<Double?>>()

        val noteByCourses = mutableListOf<ChartSeriesDto>()
        byClass.forEach { (classroom, students) ->
            val notesByPeriod = mutableListOf(ChartDto().apply { this.value = 0.0 })
            periods.forEach { (p, notes) ->
                var noteFinal = 0.0
                var studentN = 0.0
                students.forEach { s ->
                    val notesOfStudent = notes.filter { n -> n.uuidStudent == s.uuidStudent }
                    val prom = if (notesOfStudent.isEmpty()) {
                        null
                    } else {
                        var indP = 0.0
                        var indN = 0.0
                        notesOfStudent.groupBy { it.uuidSubject }.forEach { (sx, nns) ->
                            val studentSubject0Found =
                                studentSubjects.firstOrNull { ss -> ss.uuidSubject == sx && ss.uuidClassroomStudent == s.uuid && ss.period == 0 }
                            if (studentSubject0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                                indP += studentSubject0Found.recovery!!
                                indN++
                            } else {
                                val studentSubjectFound =
                                    studentSubjects.firstOrNull { ss -> ss.uuidSubject == sx && ss.uuidClassroomStudent == s.uuid && ss.period == p }
                                var inSP = 0.0
                                var inSN = 0.0
                                nns.forEach { nn ->
                                    if (nn.note != null) {
                                        inSP += nn.note!!
                                        inSN++
                                    }
                                }
                                if (inSP != 0.0) {
                                    val promBySubject =
                                        if (studentSubjectFound?.recovery != null && schoolFound.recoveryType != "at_last") {
                                            studentSubjectFound.recovery!!
                                        } else {
                                            inSP / inSN
                                        }
                                    indP += promBySubject
                                    indN++
                                }
                            }
                        }
                        if (indP == 0.0) {
                            null
                        } else {
                            indP / indN
                        }
                    }
                    if (prom != null) {
                        noteFinal += prom
                        studentN++
                    }
                    val list = perStudent.getOrPut(s.uuidStudent!!) { mutableListOf() }.toMutableList()
                    list.add(prom)
                    perStudent[s.uuidStudent!!] = list
                }
                val promFinal = if (noteFinal == 0.0) {
                    0.0
                } else {
                    noteFinal / studentN
                }
                notesByPeriod.add(ChartDto().apply { this.name = "$p Periodo"; this.value = promFinal })
            }
            noteByCourses.add(
                ChartSeriesDto().apply {
                    val classroomFound = classrooms.firstOrNull { c -> c.uuid == classroom }
                    this.name =
                        "${grades.firstOrNull { g -> classroomFound?.uuidGrade == g.uuid }?.name}-${classroomFound?.name}"
                    this.series = notesByPeriod
                }
            )
        }

        perStudent.forEach { (k, v) ->
            var sum = 0.0
            var count = 0.0
            var prom = 0.0
            v.forEach {
                if (it != null) {
                    sum += it
                    count++
                }
            }
            if (sum != 0.0) {
                prom = sum / count
            }
            if (prom > 4.55) {
                valueSuperior++
            } else {
                if (prom >= 4) {
                    valueAlto++
                } else {
                    if (prom >= 3) {
                        valueBasico++
                    } else {
                        if (prom >= 0.1) {
                            valueBajo++
                        } else {
                            valueNotDefined++
                        }
                    }
                }
            }
        }

        return HomeAdminDto().apply {
            this.performanceByCourses = noteByCourses
            this.performanceBasics = mutableListOf(
                ChartDto().apply {
                    this.name = "Superior"
                    this.value = valueSuperior
                },
                ChartDto().apply {
                    this.name = "Alto"
                    this.value = valueAlto
                },
                ChartDto().apply {
                    this.name = "Basico"
                    this.value = valueBasico
                },
                ChartDto().apply {
                    this.name = "Bajo"
                    this.value = valueBajo
                },
                ChartDto().apply {
                    this.name = "No calificados"
                    this.value = valueNotDefined
                }
            )
        }
    }

    override fun getByStudent(student: UUID): HomeAdminDto {
        val studentFound = userRepository.findById(student).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(studentFound.uuidSchool!!)
        val classroomStudent = classroomStudentRepository.findAllByUuidStudent(student)
        val classrooms = classroomRepository.findByUuidInAndYear(
            classroomStudent.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        val validClassroomStudent = classroomStudent.filter { classrooms.map { it.uuid }.contains(it.uuidClassroom) }
        val studentNotes =
            studentNoteRepository.findAllByUuidClassroomStudentIn(validClassroomStudent.mapNotNull { it.uuid })
        val classroomSubjects =
            classroomSubjectRepository.getAllByUuidClassroomIn(validClassroomStudent.mapNotNull { it.uuidClassroom })
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudent.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }
        )
        val charts = mutableListOf<ChartDto>()
        val myNotes = mutableListOf<NoteSubjectsDto>()
        val periods = classrooms.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, schoolFound.actualYear!!)
                .filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()
        subjects.forEach {
            val myNotePeriods = mutableListOf<NotePeriodDto>()
            var finalValue = 0.0
            var recoveryF: Double? = null
            val notes = studentNotes.filter { sn -> sn.uuidSubject == it.uuid }
            val studentSubject0Found = studentSubject.firstOrNull { ss -> ss.uuidSubject == it.uuid && ss.period == 0 }
            if (studentSubject0Found?.recovery != null && schoolFound.recoveryType == "at_last") {
                recoveryF = studentSubject0Found.recovery
            }
            if (notes.isNotEmpty()) {
                var vpp = 0.0
                var npp = 0
                periods.sortedBy { it.number }.forEach { p ->
                    val studentSubjectFound =
                        studentSubject.firstOrNull { ss -> ss.uuidSubject == it.uuid && ss.period == p.number }
                    val v = notes.filter { it.period == p.number }
                    var sum = 0.0
                    var nums = 0
                    var defi: Double? = null
                    v.forEach { n ->
                        if (n.note != null) {
                            sum += (n.note ?: 0.0)
                            nums++
                        }
                    }
                    if (sum != 0.0) {
                        val promBySubject =
                            if (studentSubjectFound?.recovery != null && schoolFound.recoveryType != "at_last") {
                                studentSubjectFound.recovery!!
                            } else {
                                sum / nums
                            }
                        vpp += promBySubject
                        npp++
                        defi = promBySubject
                    }
                    myNotePeriods.add(
                        NotePeriodDto().apply {
                            this.number = p.number
                            this.defi = defi
                        }
                    )
                }
                if (vpp != 0.0) {
                    finalValue = vpp / npp
                }
            }

            charts.add(
                ChartDto().apply {
                    this.name = it.name ?: ""
                    this.value = recoveryF ?: finalValue
                }
            )
            myNotes.add(
                NoteSubjectsDto().apply {
                    this.name = it.name
                    this.periods = myNotePeriods
                    this.recovery = recoveryF.toString()
                }
            )
        }
        return HomeAdminDto().apply {
            this.polarStudent = charts
            this.myNotes = myNotes
        }
    }

    override fun setNewYear(school: UUID) {
        val schoolFound = schoolService.getById(school)
        val thisYear = schoolFound.actualYear!!
        val newYear = schoolFound.actualYear!! + 1
        val toLose = schoolFound.toLose!!
        val minNote = schoolFound.minNote!!
        val grades = gradeRepository.findAllByUuidSchool(school).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(grades.mapNotNull { it.uuid }, schoolFound.actualYear!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val periods = classrooms.firstOrNull()?.uuidSchool?.let { s ->
            schoolPeriodRepository.findAllByUuidSchoolAndActualYear(s, thisYear).filter { it.init != null && it.finish != null }
        }?.sortedBy { it.number } ?: mutableListOf()
        val users = userRepository.findAllById(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }.distinct()
        )

        val periodsToCreate = periods.map {
            SchoolPeriodRequest().apply {
                this.uuidSchool = school
                this.actualYear = newYear
                this.number = it.number
                this.recovery = false
                this.finish = it.finish
                this.init = it.init
            }
        }

        val newPeriods = schoolPeriodService.saveMultiple(periodsToCreate)

        val classroomsToCreate = classrooms.map {
            ClassroomRequest().apply {
                this.uuidSchool = school
                this.year = newYear
                this.name = it.name
                this.uuidGrade = it.uuidGrade
            }
        }
        val newClassrooms = classroomService.saveMultiple(classroomsToCreate)

        val classSubToCreate = classroomSubjects.map {
            ClassroomSubjectRequest().apply {
                val antClass = classrooms.firstOrNull { a -> a.uuid == it.uuidClassroom }
                val newClass = newClassrooms.firstOrNull { n -> n.name == antClass?.name && n.uuidGrade == antClass?.uuidGrade }
                this.uuidSubject = it.uuidSubject
                this.uuidTeacher = it.uuidTeacher
                this.uuidClassroom = newClass?.uuid
            }
        }
        val formato = DecimalFormat("#.#")

        classroomSubjectService.saveMultiple(classSubToCreate)

        val classroomStudentsToModify = mutableListOf<ClassroomStudentRequest>()
        val usersToUpdateGrade = mutableMapOf<UUID, UserRequest>()

        classroomStudents.forEach { s ->
            val mySubjects = studentSubject.filter { ss -> ss.uuidStudent == s.uuidStudent }
            var loses = 0.0
            mySubjects.groupBy { it.uuidSubject }.forEach { (idSubject, subjectPeriods) ->
                var def = schoolFound.maxNote!!
                var sum = 0.0
                var notes = 0
                val ss0F = subjectPeriods.firstOrNull { sp -> sp.period == 0 }
                if (schoolFound.recoveryType == "at_last" && ss0F?.recovery != null) {
                    def = ss0F.recovery!!
                } else {
                    periods.forEach { p ->
                        val ssF = subjectPeriods.firstOrNull { sp -> sp.period == p.number }
                        if (ssF != null) {
                            val newed = if (schoolFound.recoveryType != "at_last") {
                                (ssF.recovery ?: ssF.def) ?: 0.0
                            } else {
                                ssF.def ?: 0.0
                            }
                            if (newed != 0.0) {
                                sum += formato.format(newed).replace(",", ".").toDouble()
                                notes++
                            }
                        }
                    }
                    if (sum != 0.0) {
                        def = formato.format(sum / notes).replace(",", ".").toDouble()
                    }
                }
                if (def < minNote) {
                    loses++
                }
            }
            val antClass = classrooms.firstOrNull { a -> a.uuid == s.uuidClassroom }

            if (loses >= toLose) {
                // Pierde el año
                classroomStudentsToModify.add(
                    ClassroomStudentRequest().apply {
                        val newClass = newClassrooms.firstOrNull { n -> n.name == antClass?.name && n.uuidGrade == antClass?.uuidGrade }
                        this.uuidClassroom = newClass?.uuid
                        this.uuidStudent = s.uuidStudent
                    }
                )
            } else {
                val gradeAnt = grades.firstOrNull { g -> g.uuid == antClass?.uuidGrade }
                val gradeNew = grades.firstOrNull { g -> g.ordered == gradeAnt?.ordered!! + 1 }
                if (gradeNew != null) {
                    val newClass = newClassrooms.firstOrNull { n -> n.name == antClass?.name && n.uuidGrade == gradeNew.uuid }
                    usersToUpdateGrade[s.uuidStudent!!] = UserRequest().apply { this.actualGrade = gradeNew.uuid }
                    classroomStudentsToModify.add(
                        ClassroomStudentRequest().apply {
                            this.uuidStudent = s.uuidStudent
                            this.uuidClassroom = newClass?.uuid
                        }
                    )
                }
            }
        }

        classroomStudentService.saveMultiple(classroomStudentsToModify)
        userService.updateMultiple(
            usersToUpdateGrade.map {
                UserDto().apply {
                    this.uuid = it.key
                    this.actualGrade = it.value.actualGrade
                }
            }
        )

        schoolService.update(
            school,
            SchoolRequest().apply {
                this.actualYear = newYear
            }
        )
    }
}
