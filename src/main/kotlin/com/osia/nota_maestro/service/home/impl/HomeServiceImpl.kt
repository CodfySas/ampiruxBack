package com.osia.nota_maestro.service.home.impl

import com.osia.nota_maestro.dto.home.v1.ChartDto
import com.osia.nota_maestro.dto.home.v1.ChartSeriesDto
import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.home.HomeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service("home.crud_service")
@Transactional
class HomeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val userRepository: UserRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val studentNoteRepository: StudentNoteRepository,
    private val subjectRepository: SubjectRepository
) : HomeService {

    override fun getByAdmin(school: UUID): HomeAdminDto {
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
        return HomeAdminDto().apply {
            this.studentsByGrade = chartDto
        }
    }

    override fun getByTeacher(teacher: UUID): HomeAdminDto {
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(classroomSubjects.mapNotNull { it.uuidClassroom }, LocalDateTime.now().year)
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
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
                        notesOfStudent.groupBy { it.uuidSubject }.forEach { (ss, nns) ->
                            var inSP = 0.0
                            var inSN = 0.0
                            nns.forEach { nn ->
                                if (nn.note != null) {
                                    inSP += nn.note!!
                                    inSN++
                                }
                            }
                            val promBySubject = if (inSP == 0.0) { null } else { inSP / inSN }
                            if (promBySubject != null) {
                                indP += promBySubject
                                indN ++
                            }
                        }
                        if (indP == 0.0) { null } else { indP / indN }
                    }
                    if (prom != null) {
                        noteFinal += prom
                        studentN++
                    }
                    val list = perStudent.getOrPut(s.uuidStudent!!) { mutableListOf() }.toMutableList()
                    list.add(prom)
                    perStudent[s.uuidStudent!!] = list
                }
                val promFinal = if (noteFinal == 0.0) { 0.0 } else { noteFinal / studentN }
                notesByPeriod.add(ChartDto().apply { this.name = "$p Periodo"; this.value = promFinal })
            }
            noteByCourses.add(
                ChartSeriesDto().apply {
                    val classroomFound = classrooms.firstOrNull { c -> c.uuid == classroom }
                    this.name = "${grades.firstOrNull { g -> classroomFound?.uuidGrade == g.uuid }?.name}-${classroomFound?.name}"
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
        val classroomStudent = classroomStudentRepository.findAllByUuidStudent(student)
        val classrooms = classroomRepository.findByUuidInAndYear(classroomStudent.mapNotNull { it.uuidClassroom }, LocalDateTime.now().year)
        val validClassroomStudent = classroomStudent.filter { classrooms.map { it.uuid }.contains(it.uuidClassroom) }
        val studentNotes = studentNoteRepository.findAllByUuidClassroomStudentIn(validClassroomStudent.mapNotNull { it.uuid })
        val classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(validClassroomStudent.mapNotNull { it.uuidClassroom })
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        val charts = mutableListOf<ChartDto>()
        subjects.forEach {
            var finalValue = 0.0
            val notes = studentNotes.filter { sn-> sn.uuidSubject == it.uuid }
            if(notes.isNotEmpty()){
                var vpp = 0.0
                var npp = 0
                notes.sortedBy { it.period }.groupBy { it.period }.forEach {(p,v)->
                    var sum = 0.0
                    var nums = 0
                    v.forEach { n->
                        sum += (n.note ?: 0.0)
                        nums++;
                    }
                    if(sum != 0.0){
                        vpp += (sum/nums)
                        npp++;
                    }
                }
                if(vpp != 0.0){
                    finalValue = vpp/npp
                }

            }
            charts.add(ChartDto().apply {
                this.name = it.name ?: ""
                this.value = finalValue
            })
        }
        return HomeAdminDto().apply {
            this.polarStudent = charts
        }
    }
}
