package com.osia.nota_maestro.service.recovery.impl

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryPeriodDto
import com.osia.nota_maestro.dto.recovery.v1.RecoveryStudentDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.recovery.RecoveryService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service("recovery.crud_service")
@Transactional
class RecoveryServiceImpl(
    private val userRepository: UserRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val schoolService: SchoolService,
    private val studentSubjectService: StudentSubjectService,
) : RecoveryService {

    override fun getMyRecovery(request: ResourceRequest): RecoveryDto {
        val classroom = classroomRepository.findById(request.classroom)
        val schoolFound = schoolService.getById(classroom.get().uuidSchool!!)
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(request.classroom)
        val studentSubject = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubject(classroomStudents.mapNotNull { it.uuid }, request.subject)
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val periods = schoolFound.uuid?.let { schoolPeriodRepository.findAllByUuidSchoolAndActualYear(it, schoolFound.actualYear!!) }
            ?.filter { it.init != null && it.finish != null }?.sortedBy { it.number } ?: mutableListOf()

        return RecoveryDto().apply {
            this.students = classroomStudents.map { cs ->
                RecoveryStudentDto().apply {
                    val student = students.firstOrNull { it.uuid == cs.uuidStudent }
                    val myStudentSubjects = studentSubject.filter { it.uuidClassroomStudent == cs.uuid }
                    this.uuid = cs.uuid
                    this.name = student?.name
                    this.lastname = student?.lastname
                    val ss0 = myStudentSubjects.firstOrNull { it.period == 0 }
                    this.uuidStudentSubject = ss0?.uuid
                    this.def = (ss0?.def?.toString()?.replace(".", ",") ?: "")
                    this.recovery = (ss0?.recovery?.toString()?.replace(".", ",") ?: "")
                    this.enabled = (schoolFound.recoveryType == "at_last" && schoolFound.enabledFinalRecovery == true)
                    this.periods = periods.map { p ->
                        val ssp = myStudentSubjects.firstOrNull { it.period == p.number }
                        RecoveryPeriodDto().apply {
                            this.number = p.number
                            this.def = (ssp?.def?.toString()?.replace(".", ",") ?: "")
                            this.recovery = (ssp?.recovery?.toString()?.replace(".", ",") ?: "")
                            this.enabled = (schoolFound.recoveryType != "at_last" && p.recovery == true)
                            this.uuidStudentSubject = ssp?.uuid
                        }
                    }
                }
            }
        }
    }

    override fun submitRecovery(recoveriesDto: List<RecoveryDto>): List<RecoveryDto> {
        val classroom = classroomRepository.findAllById(recoveriesDto.map { it.classroom }.distinct())
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroomIn(recoveriesDto.map { it.classroom })

        val studentSubjects = studentSubjectRepository.findAllByUuidClassroomStudentInAndUuidSubjectIn(
            classroomStudents.mapNotNull { it.uuid },
            recoveriesDto.map { it.subject }
        )
        val studentsToSave = recoveriesDto.flatMap { it.students!! }.flatMap { it.periods!! }.toMutableList()
        studentsToSave += recoveriesDto.flatMap { it.students!! }.map { s ->
            RecoveryPeriodDto().apply {
                this.uuidStudentSubject = s.uuidStudentSubject
                this.def = s.def
                this.recovery = s.recovery
                this.number = 0
            }
        }

        val toDel = (studentSubjects.filterNot { sn -> studentsToSave.mapNotNull { it.uuidStudentSubject }.contains(sn.uuid) })
        val toUpdate = mutableMapOf<UUID, StudentSubjectRequest>()
        val toCreate = mutableListOf<StudentSubjectRequest>()

        recoveriesDto.forEach { rDto ->
            rDto.students?.forEach { s ->
                val req0 = StudentSubjectRequest().apply {
                    this.uuidClassroomStudent = s.uuid
                    this.uuidSubject = rDto.subject
                    this.period = 0
                    this.uuidSchool = classroom[0]?.uuidSchool
                    this.recovery = if (s.recovery != "") {
                        s.recovery?.replace(",", ".")?.toDouble()
                    } else {
                        null
                    }
                }
                if (s.uuidStudentSubject != null) {
                    toUpdate[s.uuidStudentSubject!!] = req0
                } else {
                    toCreate.add(req0)
                }
                s.periods?.forEach { p ->
                    val reqP = StudentSubjectRequest().apply {
                        this.uuidClassroomStudent = s.uuid
                        this.uuidSubject = rDto.subject
                        this.period = p.number
                        this.uuidSchool = classroom[0]?.uuidSchool
                        this.recovery = if (p.recovery != "") {
                            p.recovery?.replace(",", ".")?.toDouble()
                        } else {
                            null
                        }
                    }
                    if (p.uuidStudentSubject != null) {
                        toUpdate[p.uuidStudentSubject!!] = reqP
                    } else {
                        toCreate.add(reqP)
                    }
                }
            }
        }

        studentSubjectService.saveMultiple(toCreate)
        studentSubjectRepository.deleteByUuids(toDel.mapNotNull { it.uuid })
        studentSubjectService.updateMultipleAndNullRecovery(
            toUpdate.map {
                StudentSubjectDto().apply {
                    this.uuid = it.key
                    this.period = it.value.period
                    this.uuidStudent = it.value.uuidStudent
                    this.uuidSubject = it.value.uuidSubject
                    this.uuidClassroomStudent = it.value.uuidClassroomStudent
                    this.recovery = it.value.recovery
                    this.uuidSchool = it.value.uuidSchool
                }
            }
        )
        return recoveriesDto
    }
}
