package com.osia.nota_maestro.service.user.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.student.v1.StudentRequest
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.dto.teacher.v1.TeacherRequest
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.model.School
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
    override fun save(userRequest: UserRequest, school: UUID, replace: Boolean): UserDto {
        log.trace("user save -> request: $userRequest")
        val actualSchool = schoolService.getById(school)
        userRequest.username = userRequest.dni + "@" + actualSchool.shortName

        val createdUser = userRepository.getFirstByUsernameOrDni(userRequest.username!!, userRequest.dni!!)
        val user = if (createdUser.isPresent){
            if(!replace){
                throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No se pudo crear el usuario, ya existe el usuario ${userRequest.username}")
            }else{
                userMapper.update(userRequest, createdUser.get())
                createdUser.get()
            }
        }else{
            userMapper.toModel(userRequest)
        }

        user.password = Md5Hash().createMd5(user.password ?: "12345")
        user.uuidSchool = actualSchool.uuid

        generateRole(userRequest, user, actualSchool.uuid!!)
        return userMapper.toDto(userRepository.save(user))
    }

    private fun saveSubModuleUsers(
        userUUID: UUID,
        moduleNames: List<String>
    ) {
        moduleUseCase.cleanSubModuleUsers(userUUID)

        val subModules = subModuleService.findAll(Pageable.unpaged())
        val usersModule = subModules.filter { moduleNames.contains(it.name) }.mapNotNull { it.uuid }
        moduleUseCase.saveSubModuleUser(SubModuleUserRequest().apply {
            this.uuidUser = userUUID
            this.uuidSubModules = usersModule
        })
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
        val actualSchool = schoolService.getById(user.uuidSchool!!)

        userRequest.username = userRequest.dni + "@" + actualSchool.shortName
        if(userRequest.dni != null && userRequest.dni != user.dni){
            val foundUser = userRepository.getFirstByUsernameOrDni(userRequest.username!!, userRequest.dni!!)
            if(foundUser.isPresent){
                throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No se puede actualizar el usuario. " +
                        "No se pudo cambiar el ${user.documentType} ${user.dni} A ${userRequest.documentType} ${userRequest.dni}," +
                        " debido a que ya existe otro usuario con ${userRequest.documentType} ${userRequest.dni} en el sistema")
            }
        }
        userMapper.update(userRequest, user)
        generateRole(userRequest, user, actualSchool.uuid!!)

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

    private fun generateRole(
        userRequest: UserRequest,
        user: User,
        actualSchoolUuid: UUID
    ) {
        if (userRequest.role == UserType.teacher) {
            saveSubModuleUsers(user.uuid!!, listOf("Usuarios"))
            val teacher = teacherService.save(
                TeacherRequest().apply {
                    this.name = user.name
                    this.lastname = user.lastname
                    this.dni = user.dni
                    this.address = userRequest.address
                    this.email = userRequest.email
                    this.phone = userRequest.phone
                    this.documentType = user.documentType
                    this.uuidSchool = actualSchoolUuid
                }, true
            )
            user.uuidRole = teacher.uuid
        }
        if (userRequest.role == UserType.student) {
            saveSubModuleUsers(user.uuid!!, listOf("Usuarios"))
            val student = studentService.save(
                StudentRequest().apply {
                    this.name = user.name
                    this.lastname = user.lastname
                    this.dni = user.dni
                    this.address = userRequest.address
                    this.email = userRequest.email
                    this.phone = userRequest.phone
                    this.documentType = user.documentType
                    this.uuidSchool = actualSchoolUuid
                }, true
            )
            user.uuidRole = student.uuid
        }
        if (user.role == UserType.admin) {
            saveSubModuleUsers(user.uuid!!, listOf("Usuarios"))
        }
    }
}
