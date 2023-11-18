package com.osia.nota_maestro.service.user.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.student.v1.StudentRequest
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.dto.teacher.v1.TeacherRequest
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.model.SubModule
import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.model.enums.UserType
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.module.ModuleUseCase
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.student.StudentService
import com.osia.nota_maestro.service.subModule.SubModuleService
import com.osia.nota_maestro.service.subModuleUser.SubModuleUserService
import com.osia.nota_maestro.service.teacher.TeacherService
import com.osia.nota_maestro.service.user.UserService
import com.osia.nota_maestro.util.CreateSpec
import com.osia.nota_maestro.util.Md5Hash
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID

@Service("user.crud_service")
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val teacherService: TeacherService,
    private val schoolService: SchoolService,
    private val userMapper: UserMapper,
    private val objectMapper: ObjectMapper,
    private val studentService: StudentService,
    private val subModuleService: SubModuleService,
    private val subModuleUserService: SubModuleUserService,
        private val moduleUseCase: ModuleUseCase


) : UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("user count -> increment: $increment")
        return userRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): User {
        var user = userRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User $uuid not found")
        }
        if (user.role == UserType.teacher) {
            val teacher = teacherService.getById(user.uuidRole!!)
            user.phone = teacher.phone
            user.address = teacher.address
            user.email = teacher.email
        }
        if (user.role == UserType.student) {
            val student = studentService.getById(user.uuidRole!!)
            user.phone = student.phone
            user.address = student.address
            user.email = student.email
        }
        return user
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<UserDto> {
        log.trace("user findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return userRepository.findAllById(uuidList).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<UserDto> {
        log.trace("user findAll -> pageable: $pageable")
        return userRepository.findAll(Specification.where(CreateSpec<User>().createSpec("", school)), pageable).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<UserDto> {
        log.trace("user findAllByFilter -> pageable: $pageable, where: $where")
        return userRepository.findAll(Specification.where(CreateSpec<User>().createSpec(where, school)), pageable).map(userMapper::toDto)
    }

    @Transactional
    override fun save(userRequest: UserRequest, school: UUID): UserDto {
        log.trace("user save -> request: $userRequest")
        val user = userMapper.toModel(userRequest)
        val actualSchool = schoolService.getById(school)
        user.username = user.username + "@" + actualSchool.shortName
        user.password = Md5Hash().createMd5(user.password ?: "12345")
        user.uuidSchool = actualSchool.uuid
        if (userRequest.role == UserType.teacher) {
            val teacher = teacherService.save(
                TeacherRequest().apply {
                    this.name = user.name
                    this.lastname = user.lastname
                    this.dni = user.dni
                    this.address = userRequest.address
                    this.email = userRequest.email
                    this.phone = userRequest.phone
                    this.documentType = user.documentType
                    this.uuidSchool = actualSchool.uuid
                }
            )
            user.uuidRole = teacher.uuid
        }
        if (userRequest.role == UserType.student) {
            val student = studentService.save(
                StudentRequest().apply {
                    this.name = user.name
                    this.lastname = user.lastname
                    this.dni = user.dni
                    this.address = userRequest.address
                    this.email = userRequest.email
                    this.phone = userRequest.phone
                    this.documentType = user.documentType
                    this.uuidSchool = actualSchool.uuid
                }
            )
            user.uuidRole = student.uuid
        }
        val subModules = subModuleService.findAll(Pageable.unpaged())
        if(user.role==UserType.admin){
            val usersModule = subModules.first { it.name=="Usuarios" }
            moduleUseCase.saveSubModuleUser(SubModuleUserRequest().apply {
                this.uuidUser = user.uuid
                this.uuidSubModules = listOf(usersModule.uuid!!)
            })
        }
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun saveMultiple(userRequestList: List<UserRequest>): List<UserDto> {
        log.trace("user saveMultiple -> requestList: ${objectMapper.writeValueAsString(userRequestList)}")
        val users = userRequestList.map(userMapper::toModel)
        return userRepository.saveAll(users).map(userMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, userRequest: UserRequest): UserDto {
        log.trace("user update -> uuid: $uuid, request: $userRequest")
        val user = getById(uuid)
        userMapper.update(userRequest, user)
        val actualSchool = schoolService.getById(user.uuidSchool!!)
        user.username = user.username + "@" + actualSchool.shortName
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun updateMultiple(userDtoList: List<UserDto>): List<UserDto> {
        log.trace("user updateMultiple -> userDtoList: ${objectMapper.writeValueAsString(userDtoList)}")
        val users = userRepository.findAllById(userDtoList.mapNotNull { it.uuid })
        users.forEach { user ->
            userMapper.update(userMapper.toRequest(userDtoList.first { it.uuid == user.uuid }), user)
        }
        return userRepository.saveAll(users).map(userMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("user delete -> uuid: $uuid")
        val user = getById(uuid)
        user.deleted = true
        user.deletedAt = LocalDateTime.now()
        userRepository.save(user)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("user deleteMultiple -> uuid: $uuidList")
        val users = userRepository.findAllById(uuidList)
        users.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        userRepository.saveAll(users)
    }
}
