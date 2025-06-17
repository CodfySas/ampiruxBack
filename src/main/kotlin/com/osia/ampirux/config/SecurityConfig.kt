package com.osia.template.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
        http.cors()
        http.authorizeRequests().antMatchers("/protectedUri").authenticated()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // Permite cualquier origen. Puedes ajustar según tu necesidad
        // Cambiá esto de "*"
        configuration.allowedOrigins = listOf("http://localhost:4200")

        // Métodos HTTP permitidos
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")

        // Permite cualquier header
        configuration.allowedHeaders = listOf("*")

        // Permite credenciales si las necesitas (cookies, auth headers, etc.)
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}
