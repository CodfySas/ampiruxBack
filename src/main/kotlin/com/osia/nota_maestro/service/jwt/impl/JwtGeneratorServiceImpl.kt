package com.osia.nota_maestro.service.jwt.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.service.jwt.JwtGenerator
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service("jwt_service")
@Transactional
class JwtGeneratorServiceImpl(
    private val objectMapper: ObjectMapper
) : JwtGenerator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun generateToken(user: UserDto): String {
        log.trace("auth login -> userRequest: ${objectMapper.writeValueAsString(user)}")
        return Jwts.builder().setSubject(user.uuidCompany.toString()).setIssuedAt(Date())
            .signWith(SignatureAlgorithm.HS256, "secret").compact()
    }
}
