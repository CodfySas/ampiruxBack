package com.osia.nota_maestro.service.user.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.subModuleUser.v1.SubModuleUserRequest
import com.osia.nota_maestro.dto.user.v1.ChangePasswordRequest
import com.osia.nota_maestro.dto.user.v1.NotSavedUserDto
import com.osia.nota_maestro.dto.user.v1.SavedMultipleUserDto
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.module.ModuleUseCase
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.subModule.SubModuleService
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
import kotlin.reflect.full.memberProperties

@Service("user.crud_service")
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val schoolService: SchoolService,
    private val userMapper: UserMapper,
    private val objectMapper: ObjectMapper,
    private val subModuleService: SubModuleService,
    private val moduleUseCase: ModuleUseCase

) : UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("user count -> increment: $increment")
        return userRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID, includeDelete: Boolean): User {
        return if (includeDelete) {
            userRepository.getByUuid(uuid).get()
        } else {
            userRepository.findById(uuid).orElseThrow {
                ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User $uuid not found")
            }
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<UserDto> {
        log.trace("user findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return userRepository.findAllById(uuidList).map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<UserDto> {
        log.trace("user findAll -> pageable: $pageable")
        return userRepository.findAll(Specification.where(CreateSpec<User>().createSpec("", school)), pageable)
            .map(userMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<UserDto> {
        log.trace("user findAllByFilter -> pageable: $pageable, where: $where")
        return userRepository.findAll(Specification.where(CreateSpec<User>().createSpec(where, school)), pageable)
            .map(userMapper::toDto)
    }

    @Transactional
    override fun save(userRequest: UserRequest, school: UUID, replace: Boolean): UserDto {
        log.trace("user save -> request: $userRequest")
        val actualSchool = schoolService.getById(school)
        userRequest.username = userRequest.dni + "@" + actualSchool.shortName

        val newPass = userRequest.password?.let { Md5Hash().createMd5(it) }

        val createdUser = userRepository.getFirstByUsernameOrDni(userRequest.username!!, userRequest.dni!!)
        val user = if (createdUser.isPresent) {
            if (!replace) {
                throw ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No se pudo crear el usuario, ya existe el usuario ${userRequest.username}"
                )
            } else {
                userMapper.update(userRequest, createdUser.get())
                if (newPass != null) {
                    createdUser.get().password = newPass
                }
                createdUser.get()
            }
        } else {
            val model = userMapper.toModel(userRequest)
            model.password = (newPass ?: Md5Hash().createMd5("12345"))
            model
        }
        user.uuidSchool = actualSchool.uuid

        generateRole(user.role ?: "student", user)
        return userMapper.toDto(userRepository.save(user))
    }

    private fun saveSubModuleUsers(
        userUUID: UUID,
        moduleNames: List<String>
    ) {
        moduleUseCase.cleanSubModuleUsers(userUUID)

        val subModules = subModuleService.findAll(Pageable.unpaged())
        val usersModule = subModules.filter { moduleNames.contains(it.name) }.mapNotNull { it.uuid }
        moduleUseCase.saveSubModuleUser(
            SubModuleUserRequest().apply {
                this.uuidUser = userUUID
                this.uuidSubModules = usersModule
            }
        )
    }

    @Transactional
    override fun saveMultiple(userRequestList: List<UserRequest>, school: UUID, role: String): SavedMultipleUserDto {
        log.trace("user saveMultiple -> requestList: ${objectMapper.writeValueAsString(userRequestList)}")
        val types = listOf("cc", "ti", "de", "rc", "nit", "pa", "ot")
        val notSaved = mutableListOf<NotSavedUserDto>()
        val savedSuccess = mutableListOf<UserDto>()
        userRequestList.forEach {
            var validUser = false
            val properties = it::class.memberProperties
            for (property in properties) {
                val value = property.getter.call(it)
                if (value != null) {
                    validUser = true
                }
            }
            if (validUser) {
                if (it.documentType == null || it.documentType == "" || it.dni == null || it.dni == "") {
                    notSaved.add(
                        NotSavedUserDto().apply {
                            this.user = it
                            this.reason = "No se envió la informacion completa del documento de identidad"
                        }
                    )
                } else {
                    val myDoc = getDocumentTypeByExamples(it.documentType!!)
                    if (!types.contains(myDoc)) {
                        notSaved.add(
                            NotSavedUserDto().apply {
                                this.user = it
                                this.reason = "No se reconoce el tipo de documento ${it.documentType}"
                            }
                        )
                    } else {
                        try {
                            it.role = role
                            it.documentType = myDoc
                            val saved = this.save(it, school, true)
                            savedSuccess.add(saved)
                        } catch (e: Exception) {
                            log.info("error creando un usuario -> ${e.message}")
                            notSaved.add(
                                NotSavedUserDto().apply {
                                    this.user = it
                                    this.reason = e.message
                                }
                            )
                        }
                    }
                }
            }
        }
        return SavedMultipleUserDto().apply {
            this.notSavedUsers = notSaved
            this.savedUsers = savedSuccess
        }
    }

    @Transactional
    override fun update(uuid: UUID, userRequest: UserRequest): UserDto {
        log.trace("user update -> uuid: $uuid, request: $userRequest")
        val user = getById(uuid)
        val actualSchool = schoolService.getById(user.uuidSchool!!)

        if (userRequest.dni != null) {
            userRequest.username = userRequest.dni + "@" + actualSchool.shortName
        }
        if (userRequest.dni != null && userRequest.dni != user.dni) {
            val foundUser = userRepository.getFirstByUsernameOrDni(userRequest.username!!, userRequest.dni!!)
            if (foundUser.isPresent) {
                throw ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "No se puede actualizar el usuario. " +
                        "No se pudo cambiar el ${user.documentType} ${user.dni} A ${userRequest.documentType} ${userRequest.dni}," +
                        " debido a que ya existe otro usuario con ${userRequest.documentType} ${userRequest.dni} en el sistema"
                )
            }
        }
        userMapper.update(userRequest, user)
        generateRole(user.role ?: "students", user)

        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    override fun changePassword(changePasswordRequest: ChangePasswordRequest): UserDto {
        val user = this.getById(changePasswordRequest.uuidUser!!)
        if (user.password == Md5Hash().createMd5(changePasswordRequest.actualPassword ?: "")) {
            user.password = Md5Hash().createMd5(changePasswordRequest.password!!)
        } else {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "La contraseña actual no es correcta")
        }
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
        role: String,
        user: User,
    ) {
        if (role == "teacher") {
            saveSubModuleUsers(
                user.uuid!!,
                listOf(
                    "Notas", "Archivo De Notas", "Asistencias", "Aula y Recursos", "Pre Informes",
                    "Recuperaciones", "Malla academica", "Evaluaciones", "Foro Publico",
                    "Mi Grupo", "Certificados", "Acompañamientos", "Director de grupo", "Mis Cursos", "Planeacion", "Plan de Estudios", "Diagnostico"
                )
            )
        }
        if (role == "student") {
            saveSubModuleUsers(user.uuid!!, listOf("Boletin", "Certificados", "Mis Asistencias", "Malla academica", "Plan de Estudios", "Mis Clases", "Foro Publico"))
        }
        if (role == "admin") {
            saveSubModuleUsers(
                user.uuid!!,
                listOf(
                    "Usuarios", "Administradores", "Asistencias", "Estudiantes", "Diagnostico", "Pre Informes",
                    "Docentes", "Cursos", "Asignaturas", "Asignar Docentes", "Archivo De Notas",
                    "Aula y Recursos", "Documentacion", "Boletines", "Directores grupo", "Foro Publico",
                    "Planificacion", "Horarios", "Certificados", "Malla academica", "Acompañantes", "Planeacion", "Plan de Estudios"
                )
            )
        }
    }

    private fun getDocumentTypeByExamples(string: String): String {
        val lower = string.lowercase()
        if (lower.contains("ced", true)) {
            return "cc"
        }
        if (lower.contains("tarj", true) || lower.contains("id", true)) {
            return "ti"
        }
        if (lower.contains("ext", true)) {
            return "de"
        }
        if (lower.contains("reg", true) || lower.contains("civ", true)) {
            return "rc"
        }
        if (lower.contains("pas", true)) {
            return "pa"
        }
        if (lower.contains("otr", true)) {
            return "ot"
        }
        return lower
    }
}
