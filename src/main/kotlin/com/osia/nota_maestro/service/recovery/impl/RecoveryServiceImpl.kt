package com.osia.nota_maestro.service.recovery.impl

import com.osia.nota_maestro.dto.recovery.v1.RecoveryClassroomDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryGradeDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryPeriodDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryStudentDto
import com.osia.nota_maestro.dto.recovery.v1.RecoverySubjectsDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.recovery.RecoveryService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service("recovery.crud_service")
@Transactional
class RecoveryServiceImpl(
    private val gradeRepository: GradeRepository,
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val schoolService: SchoolService,
    private val studentSubjectService: StudentSubjectService
) : RecoveryService {

    override fun getMyRecovery(teacher: UUID): RecoveryDto {
        val teacherFound = userRepository.findById(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(
            classroomSubjects.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        return returnNotes(classrooms, classroomSubjects, schoolFound.actualYear!!, schoolFound.recoveryType!!, schoolFound.enabledFinalRecovery!!)
    }

    private fun returnNotes(
        classrooms: List<Classroom>,
        classroomSubjects: List<ClassroomSubject>,
        year: Int,
        recoveryType: String,
        enabledFinalRecovery: Boolean
    ): RecoveryDto {
        val classroomStudents = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })
        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            classroomSubjects.mapNotNull { it.uuidSubject }
        )
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        val periods = grades.firstOrNull()?.uuidSchool?.let { schoolPeriodRepository.findAllByUuidSchoolAndActualYear(it, year) }
            ?.filter { it.init != null && it.finish != null }?.sortedBy { it.number } ?: mutableListOf()

        return RecoveryDto().apply {
            this.grades = grades.map { g ->
                RecoveryGradeDto().apply {
                    this.uuid = g.uuid
                    this.name = g.name
                    this.classrooms = classrooms.filter { c -> c.uuidGrade == g.uuid }.map { c ->
                        RecoveryClassroomDto().apply {
                            this.uuid = c.uuid
                            this.name = c.name
                            this.students = classroomStudents.filter { cs -> cs.uuidClassroom == c.uuid }.map { cs ->
                                RecoveryStudentDto().apply {
                                    val student = students.first { it.uuid == cs.uuidStudent }
                                    this.uuid = cs.uuidStudent
                                    this.name = student.name
                                    this.lastname = student.lastname
                                    this.code = student.code
                                    this.subjects =
                                        classroomSubjects.filter { cx -> cx.uuidClassroom == cs.uuidClassroom }
                                            .map { cx ->
                                                RecoverySubjectsDto().apply {
                                                    this.uuid = cx.uuidSubject
                                                    this.name = subjects.first { it.uuid == cx.uuidSubject }.name
                                                    this.enabled = (recoveryType == "at_last" && enabledFinalRecovery)
                                                    val recoveryFound = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == 0 }
                                                    this.recovery = recoveryFound?.recovery?.toString()?.replace(".",",")
                                                    this.def = recoveryFound?.def?.toString()?.replace(".",",")
                                                    this.uuidStudentSubject = recoveryFound?.uuid
                                                    this.periods = periods.map { p ->
                                                        RecoveryPeriodDto().apply {
                                                            val studentSubjectFound = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == p.number }
                                                            this.def = studentSubjectFound?.def?.toString()?.replace(".",",")
                                                            this.recovery = studentSubjectFound?.recovery?.toString()?.replace(".",",")
                                                            this.uuidStudentSubject = studentSubjectFound?.uuid
                                                            this.enabled = recoveryType != "at_last" && p.recovery == true
                                                            this.number = p.number
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

    override fun submitRecovery(recoveryDto: RecoveryDto, teacher: UUID): RecoveryDto {
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
        val submitPeriods =
            recoveryDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
                .flatMap { it.periods!! }.map { it.number!! }
        val allStudentSubjects = recoveryDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }
            .flatMap { it.periods!! }.toMutableList()
        val studentSubject0 = recoveryDto.grades!!.flatMap { it.classrooms!! }.flatMap { it.students!! }.flatMap { it.subjects!! }.map { RecoveryPeriodDto().apply {
            this.uuidStudentSubject = it.uuidStudentSubject
            this.def = it.def
            this.recovery = it.recovery
        } }
        allStudentSubjects+=studentSubject0

        val toDelSS = (studentSubjects.filterNot { ss -> allStudentSubjects.map { it.uuidStudentSubject }.contains(ss.uuid) })
            .filter { submitPeriods.contains(it.period) }
        val toUpdateSS = mutableMapOf<UUID, StudentSubjectRequest>()
        val toCreateSS = mutableListOf<StudentSubjectRequest>()

        recoveryDto.grades?.forEach { g ->
            g.classrooms?.forEach { c ->
                c.students?.forEach { s ->
                    val cs = classroomStudents.first { cst -> cst.uuidStudent == s.uuid && cst.uuidClassroom == c.uuid }
                    s.subjects?.forEach { u ->
                        val reqSS0 = StudentSubjectRequest().apply {
                            this.uuidClassroomStudent = cs.uuid
                            this.uuidSchool = userFound.uuidSchool
                            this.uuidSubject = u.uuid
                            this.uuidStudent = s.uuid
                            this.period = 0
                            this.def = u.def?.replace(",",".")?.toDoubleOrNull()
                            this.recovery = u.recovery?.replace(",",".")?.toDoubleOrNull()
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
                                this.def = p.def?.replace(",",".")?.toDoubleOrNull()
                                this.recovery = p.recovery?.replace(",",".")?.toDoubleOrNull()
                            }
                            if (p.uuidStudentSubject != null) {
                                toUpdateSS[p.uuidStudentSubject!!] = reqSS
                            } else {
                                toCreateSS.add(reqSS)
                            }
                        }
                    }
                }
            }
        }

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

        return recoveryDto
    }
}
