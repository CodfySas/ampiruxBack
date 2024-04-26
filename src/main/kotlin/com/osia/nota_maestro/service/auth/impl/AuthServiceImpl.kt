package com.osia.nota_maestro.service.auth.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomDto
import com.osia.nota_maestro.dto.grade.v1.GradeDto
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodMapper
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.auth.AuthUseCase
import com.osia.nota_maestro.service.classroom.ClassroomService
import com.osia.nota_maestro.service.grade.GradeService
import com.osia.nota_maestro.service.jwt.JwtGenerator
import com.osia.nota_maestro.util.Md5Hash
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service("auth.crud_service")
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val jwtGenerator: JwtGenerator,
    private val userMapper: UserMapper,
    private val schoolRepository: SchoolRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val schoolPeriodMapper: SchoolPeriodMapper,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val gradeService: GradeService,
    private val classroomService: ClassroomService
) : AuthUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun login(userRequest: UserRequest): UserDto {
        log.trace("auth login -> userRequest: ${objectMapper.writeValueAsString(userRequest)}")
        if (userRequest.username == null || userRequest.password == null ||
            userRequest.username == "" || userRequest.password == ""
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and Password is required")
        }

        val pass = Md5Hash().createMd5(userRequest.password!!)
        userRequest.username = userRequest.username!!.lowercase()
        val userFound = userRepository.getFirstByUsernameAndPassword(userRequest.username!!, pass).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid credentials")
        }

        val school = schoolRepository.findById(userFound.uuidSchool!!).get()
        if (userFound.role != "admin") {
            if ((userFound.role == "student" && school.enabledStudent == false) || (userFound.role == "teacher" && school.enabledTeacher == false)) {
                throw ResponseStatusException(HttpStatus.LOCKED, "Invalid credentials")
            }
        }

        if(userFound.active == false){
            throw ResponseStatusException(HttpStatus.LOCKED, "Invalid credentials")
        }
        val periodList = schoolPeriodRepository.findAllByUuidSchoolAndActualYear(school.uuid!!, school.actualYear!!)

        var grades = mutableListOf<GradeDto>()
        var classrooms = mutableListOf<ClassroomDto>()
        if (userFound.role == "student") {
            val cs = classroomStudentRepository.findAllByUuidStudent(userFound.uuid!!)
            classrooms = classroomService.findByMultiple(cs.mapNotNull { it.uuidClassroom }.distinct()).toMutableList()
            grades = gradeService.findByMultiple(classrooms.mapNotNull { it.uuidGrade }.distinct()).toMutableList()
        }

        return userMapper.toDto(userFound).apply {
            this.token = jwtGenerator.generateToken(userMapper.toDto(userFound))
            this.schoolName = school.name
            this.color1 = school.color1
            this.color2 = school.color2
            this.shortName = school.shortName
            this.periods = school.periods
            this.periodList = periodList.map(schoolPeriodMapper::toDto).sortedBy { it.number }
            this.actualYear = school.actualYear
            this.enabledStudent = school.enabledStudent
            this.enabledTeacher = school.enabledTeacher
            this.maxNote = school.maxNote
            this.minNote = school.minNote
            this.toLose = school.toLose
            this.recoveryType = school.recoveryType
            this.enabledFinalRecovery = school.enabledFinalRecovery
            this.classroom = "${grades.firstOrNull()?.name}-${classrooms.firstOrNull()?.name}"
            this.reportType = school.reportType
            this.planningType = school.planningType
        }
    }
}
