package com.osia.nota_maestro.service.auth.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.dto.user.v1.UserRequest
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.auth.AuthUseCase
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
    private val schoolRepository: SchoolRepository
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

        return userMapper.toDto(userFound).apply {
            this.token = jwtGenerator.generateToken(userMapper.toDto(userFound))
            this.schoolName = school.name
            this.color1 = school.color1
            this.color2 = school.color2
        }
    }
}
