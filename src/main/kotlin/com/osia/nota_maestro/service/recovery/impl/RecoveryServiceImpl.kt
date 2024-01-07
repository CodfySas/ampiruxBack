package com.osia.nota_maestro.service.recovery.impl

import com.osia.nota_maestro.dto.recovery.v1.RecoveryClassroomDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryGradeDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryPeriodDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryStudentDto
import com.osia.nota_maestro.dto.recovery.v1.RecoverySubjectsDto
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
import com.osia.nota_maestro.service.recovery.RecoveryService
import com.osia.nota_maestro.service.school.SchoolService
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
    private val studentNoteRepository: StudentNoteRepository,
    private val judgmentRepository: JudgmentRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val schoolService: SchoolService
) : RecoveryService {

    override fun getMyNotes(teacher: UUID): RecoveryDto {
        val teacherFound = userRepository.findById(teacher).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(teacher)
        val classrooms = classroomRepository.findByUuidInAndYear(
            classroomSubjects.mapNotNull { it.uuidClassroom },
            schoolFound.actualYear!!
        )
        return returnNotes(classrooms, classroomSubjects, schoolFound.actualYear!!, schoolFound.recoveryType!!)
    }

    private fun returnNotes(
        classrooms: List<Classroom>,
        classroomSubjects: List<ClassroomSubject>,
        year: Int,
        recoveryType: String
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
            ?.filter { it.init != null && it.finish != null } ?: mutableListOf()

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
                                                    this.enabled = recoveryType == "at_last"
                                                    val recoveryFound = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == 0 }
                                                    this.recovery = recoveryFound?.recovery
                                                    this.def = recoveryFound?.def
                                                    this.uuidStudentSubject = recoveryFound?.uuid
                                                    this.periods = periods.map { p ->
                                                        RecoveryPeriodDto().apply {
                                                            val studentSubjectFound = studentSubjects.firstOrNull { ss -> ss.uuidSubject == cx.uuidSubject && ss.uuidClassroomStudent == cs.uuid && ss.period == p.number }
                                                            this.def = studentSubjectFound?.def
                                                            this.recovery = studentSubjectFound?.recovery
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

    override fun submitNotes(noteDto: RecoveryDto, teacher: UUID): RecoveryDto {
        return RecoveryDto()
        /*
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
            .flatMap { it.periods!! }

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
        return noteDto*/
    }
}
