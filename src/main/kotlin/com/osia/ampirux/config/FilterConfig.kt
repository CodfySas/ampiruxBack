package com.osia.ampirux.config

import com.osia.ampirux.filter.JwtFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Configuration
class FilterConfig {
    @Bean
    fun jwtFilter(): FilterRegistrationBean<*> {
        return try {
            val filter: FilterRegistrationBean<*> = FilterRegistrationBean<JwtFilter>()
            filter.filter = JwtFilter()
            filter.addUrlPatterns("/v1/*")
            filter
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
        }
    }
}
