package com.osia.nota_maestro.service.attendance.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.attendance.v1.AttendanceCompleteDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceMapper
import com.osia.nota_maestro.dto.attendance.v1.AttendanceRequest
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailDto
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailRequest
import com.osia.nota_maestro.dto.resources.v1.ResourceClassroomDto
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.dto.resources.v1.ResourceSubjectDto
import com.osia.nota_maestro.model.Attendance
import com.osia.nota_maestro.repository.attendance.AttendanceRepository
import com.osia.nota_maestro.repository.attendanceFail.AttendanceFailRepository
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.attendance.AttendanceService
import com.osia.nota_maestro.service.attendanceFail.AttendanceFailService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

@Service("attendance.crud_service")
@Transactional
class AttendanceServiceImpl(
    private val attendanceRepository: AttendanceRepository,
    private val attendanceMapper: AttendanceMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolService: SchoolService,
    private val attendanceFailRepository: AttendanceFailRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomRepository: ClassroomRepository,
    private val gradeRepository: GradeRepository,
    private val subjectRepository: SubjectRepository,
    private val attendanceFailService: AttendanceFailService
) : AttendanceService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("attendance count -> increment: $increment")
        return attendanceRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Attendance {
        return attendanceRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Attendance $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<AttendanceDto> {
        log.trace("attendance findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return attendanceRepository.findAllById(uuidList).map(attendanceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<AttendanceDto> {
        log.trace("attendance findAll -> pageable: $pageable")
        return attendanceRepository.findAll(
            Specification.where(CreateSpec<Attendance>().createSpec("", school)),
            pageable
        ).map(attendanceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AttendanceDto> {
        log.trace("attendance findAllByFilter -> pageable: $pageable, where: $where")
        return attendanceRepository.findAll(
            Specification.where(CreateSpec<Attendance>().createSpec(where, school)),
            pageable
        ).map(attendanceMapper::toDto)
    }

    @Transactional
    override fun save(attendanceRequest: AttendanceRequest, replace: Boolean): AttendanceDto {
        log.trace("attendance save -> request: $attendanceRequest")
        val savedAttendance = attendanceMapper.toModel(attendanceRequest)
        return attendanceMapper.toDto(attendanceRepository.save(savedAttendance))
    }

    @Transactional
    override fun saveMultiple(attendanceRequestList: List<AttendanceRequest>): List<AttendanceDto> {
        log.trace("attendance saveMultiple -> requestList: ${objectMapper.writeValueAsString(attendanceRequestList)}")
        val attendances = attendanceRequestList.map(attendanceMapper::toModel)
        return attendanceRepository.saveAll(attendances).map(attendanceMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, attendanceRequest: AttendanceRequest, includeDelete: Boolean): AttendanceDto {
        log.trace("attendance update -> uuid: $uuid, request: $attendanceRequest")
        val attendance = if (!includeDelete) {
            getById(uuid)
        } else {
            attendanceRepository.getByUuid(uuid).get()
        }
        attendanceMapper.update(attendanceRequest, attendance)
        return attendanceMapper.toDto(attendanceRepository.save(attendance))
    }

    @Transactional
    override fun updateMultiple(attendanceDtoList: List<AttendanceDto>): List<AttendanceDto> {
        log.trace("attendance updateMultiple -> attendanceDtoList: ${objectMapper.writeValueAsString(attendanceDtoList)}")
        val attendances = attendanceRepository.findAllById(attendanceDtoList.mapNotNull { it.uuid })
        attendances.forEach { attendance ->
            attendanceMapper.update(
                attendanceMapper.toRequest(attendanceDtoList.first { it.uuid == attendance.uuid }),
                attendance
            )
        }
        return attendanceRepository.saveAll(attendances).map(attendanceMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("attendance delete -> uuid: $uuid")
        val attendance = getById(uuid)
        attendance.deleted = true
        attendance.deletedAt = LocalDateTime.now()
        attendanceRepository.save(attendance)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("attendance deleteMultiple -> uuid: $uuidList")
        val attendances = attendanceRepository.findAllById(uuidList)
        attendances.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        attendanceRepository.saveAll(attendances)
    }

    override fun getComplete(classroom: UUID, subject: UUID, month: Int, school: UUID): List<AttendanceCompleteDto> {
        val schoolF = schoolService.getById(school)
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(classroom)
        val users = userRepository.findAllById(classroomStudents.mapNotNull { it.uuidStudent })
        log.trace("attendance getComplete -> classroom: $classroom")
        val attendances = attendanceRepository.getAllByUuidClassroomAndUuidSubjectAndMonth(classroom, subject, month)
            .sortedBy { it.day }
        val fails = attendanceFailRepository.getAllByUuidAttendanceIn(attendances.mapNotNull { it.uuid })
        val yearMonth = YearMonth.of(schoolF.actualYear!!, month)
        return classroomStudents.map { cs ->
            AttendanceCompleteDto().apply {
                val user = users.firstOrNull { u -> u.uuid == cs.uuidStudent }
                val attendanceList = mutableListOf<AttendanceDto>()
                for (i in 1..yearMonth.lengthOfMonth()) {
                    attendanceList.add(
                        AttendanceDto().apply {
                            val found = attendances.firstOrNull { a ->
                                a.uuidClassroom == classroom && a.uuidSubject == subject &&
                                    a.day == i && a.month == month
                            }
                            val fail =
                                found?.let { fails.firstOrNull { f -> f.uuidAttendance == it.uuid && f.uuidStudent == cs.uuidStudent } }
                            this.day = i
                            this.uuid = found?.uuid
                            this.month = month
                            this.uuidClassroom = classroom
                            this.uuidSubject = subject
                            this.week =
                                LocalDate.of(schoolF.actualYear!!, month, i).dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                            this.uuidSchool = school
                            this.enabled = found != null

                            this.failed = (fail != null)
                            this.reason = fail?.reason
                            this.failUuid = fail?.uuid
                        }
                    )
                }
                this.name = user?.name
                this.uuid = user?.uuid
                this.lastname = user?.lastname
                this.attendances = attendanceList
            }
        }
    }

    override fun submit(
        classroom: UUID,
        subject: UUID,
        month: Int,
        school: UUID,
        req: List<AttendanceCompleteDto>
    ): List<AttendanceCompleteDto> {
        val toSave = mutableListOf<AttendanceRequest>()
        val toUpdate = mutableListOf<AttendanceDto>()
        val toDelete = mutableListOf<UUID>()
        req.firstOrNull()?.attendances?.forEach { fd ->
            if (fd.enabled == true) {
                if (fd.uuid != null) {
                    toUpdate.add(
                        AttendanceDto().apply {
                            this.uuid = fd.uuid
                            this.day = fd.day
                            this.month = month
                            this.uuidClassroom = classroom
                            this.uuidSubject = subject
                            this.uuidSchool = school
                        }
                    )
                } else {
                    toSave.add(
                        AttendanceRequest().apply {
                            this.day = fd.day
                            this.month = month
                            this.uuidClassroom = classroom
                            this.uuidSubject = subject
                            this.uuidSchool = school
                        }
                    )
                }
            } else {
                if (fd.uuid != null) {
                    toDelete.add(fd.uuid!!)
                }
            }
        }
        val savedNews = saveMultiple(toSave)
        val savedUpdates = updateMultiple(toUpdate)
        deleteMultiple(toDelete)

        val fullAttendances = savedNews + savedUpdates

        val toSaveFail = mutableListOf<AttendanceFailRequest>()
        val toUpdateFail = mutableListOf<AttendanceFailDto>()
        val toDeleteFail = mutableListOf<UUID>()
        req.forEach { student ->
            student.attendances.forEach { day ->
                if (day.failed == true) {
                    if (day.failUuid == null) {
                        toSaveFail.add(
                            AttendanceFailRequest().apply {
                                this.reason = day.reason
                                this.uuidStudent = student.uuid
                                this.uuidAttendance = fullAttendances.firstOrNull {
                                    it.day == day.day
                                }?.uuid
                            }
                        )
                    } else {
                        toUpdateFail.add(
                            AttendanceFailDto().apply {
                                this.uuid = day.failUuid
                                this.reason = day.reason
                                this.uuidStudent = student.uuid
                                this.uuidAttendance = fullAttendances.firstOrNull {
                                    it.day == day.day
                                }?.uuid
                            }
                        )
                    }
                } else {
                    if (day.failUuid != null) {
                        toDeleteFail.add(day.failUuid!!)
                    }
                }
            }
        }
        attendanceFailService.saveMultiple(toSaveFail)
        attendanceFailService.updateMultiple(toUpdateFail)
        attendanceFailService.deleteMultiple(toDeleteFail)
        return req
    }

    override fun getResources(uuid: UUID): List<ResourceGradeDto> {
        val teacherFound = userRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolFound = schoolService.getById(teacherFound.uuidSchool!!)
        var classroomSubjects = classroomSubjectRepository.getAllByUuidTeacher(uuid)
        val classrooms = if (teacherFound.role == "teacher") {
            classroomRepository.findByUuidInAndYear(
                classroomSubjects.mapNotNull { it.uuidClassroom },
                schoolFound.actualYear!!
            )
        } else {
            classroomRepository.findAllByUuidSchoolAndYear(schoolFound.uuid!!, schoolFound.actualYear!!)
        }
        if (teacherFound.role != "teacher") {
            classroomSubjects = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        }
        val grades =
            gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade }.distinct()).sortedBy { it.ordered }
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())

        return grades.map { g ->
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
