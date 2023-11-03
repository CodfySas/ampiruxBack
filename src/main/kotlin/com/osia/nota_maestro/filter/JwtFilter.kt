package com.osia.nota_maestro.filter

import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter : GenericFilterBean() {
    private val log = LoggerFactory.getLogger(javaClass)
    @Throws(IOException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val servletRequest = request as HttpServletRequest
        val servletResponse = response as HttpServletResponse
        try {
            val authHeader = servletRequest.getHeader("authorization")
            val school = servletRequest.getHeader("school")
            if ("OPTIONS" == servletRequest.method) {
                servletResponse.status = HttpServletResponse.SC_OK
                chain?.doFilter(servletRequest, servletResponse)
            } else {
                if (authHeader == null || !authHeader.startsWith("Bearer ") || school == null) {
                    servletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
                }
                val token = authHeader.substring(7)
                if (Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body.subject != school) {
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
                }
            }
            val token = authHeader!!.substring(7)
            val claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body
            servletRequest.setAttribute("claims", claims)
            servletRequest.setAttribute("blog", servletRequest.getParameter("id"))
            chain!!.doFilter(servletRequest, servletResponse)
        } catch (e: ResponseStatusException) {

            val sb = StringBuilder()
            sb.append("{ ")
            sb.append("\"error\": \"Unauthorized\" ")
            sb.append("\"message\": \"Invalid session, please restart log in\"")
            sb.append("\"path\": \"")
                .append(request.requestURL)
                .append("\"")
            sb.append("} ")

            response.setContentType("application/json")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.getWriter().write(sb.toString())
            return
        }
    }
}
