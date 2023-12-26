package com.osia.nota_maestro.service.home.impl

import com.osia.nota_maestro.dto.home.v1.ChartDto
import com.osia.nota_maestro.dto.home.v1.ChartSeriesDto
import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.home.HomeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.DecimalFormat
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

        val formato = DecimalFormat("#.#")
        formato.maximumFractionDigits = 1

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

        return HomeAdminDto().apply {
            this.performanceByCourses = noteByCourses
        }
    }
}
