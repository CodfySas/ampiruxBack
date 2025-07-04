package com.osia.ampirux.service.auth.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.user.v1.UserDto
import com.osia.ampirux.dto.user.v1.UserMapper
import com.osia.ampirux.dto.user.v1.UserRequest
import com.osia.ampirux.repository.user.UserRepository
import com.osia.ampirux.service.auth.AuthUseCase
import com.osia.ampirux.service.barbershop.BarberShopService
import com.osia.ampirux.service.jwt.JwtGenerator
import com.osia.ampirux.service.user.UserService
import com.osia.ampirux.util.Md5Hash
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
    private val userService: UserService,
    private val barberShopService: BarberShopService,
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
        val userFound = userRepository.getFirstByUsernameIgnoreCaseOrEmailIgnoreCase(userRequest.username!!, userRequest.username!!).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid credentials")
        }

        if (userFound.password != pass) {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid credentials")
        }

        if (userFound.active == false) {
            throw ResponseStatusException(HttpStatus.LOCKED, "Invalid credentials")
        }

        val barber = userFound.barbershopUuid?.let { barberShopService.getById(it) }

        return userMapper.toDto(userFound).apply {
            this.token = jwtGenerator.generateToken(userMapper.toDto(userFound))
            this.barbershopCode = barber?.code
        }
    }

    override fun signUp(userRequest: UserRequest): UserDto {
        log.trace("auth login -> userRequest: ${objectMapper.writeValueAsString(userRequest)}")
        val userFound = userRepository.getFirstByUsernameIgnoreCaseOrEmailIgnoreCase(userRequest.username!!, userRequest.username!!)

        if (userFound.isPresent) {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username or Password already exist")
        }
        val pass = Md5Hash().createMd5(userRequest.password!!)
        val savedUser = this.userService.save(
            userRequest.apply {
                this.password = pass
            }
        )
        return savedUser.apply {
            this.token = jwtGenerator.generateToken(savedUser)
        }
    }

    override fun checkUsername(username: String): Boolean {
        val userFound = userRepository.getFistByUsernameIgnoreCase(username)
        return userFound.isPresent
    }

    override fun checkEmail(email: String): Boolean {
        val userFound = userRepository.getFistByEmailIgnoreCase(email)
        return userFound.isPresent
    }
}
