package org.example.vrgallery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.session.MapSessionRepository
import java.util.concurrent.ConcurrentHashMap


@SpringBootApplication()
//@EnableAspectJAutoProxy
class VRgalleryApplication{
    @Bean
    fun sessionRepository(): MapSessionRepository {
        return MapSessionRepository(ConcurrentHashMap())
    }
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/manage/**").authenticated()
                    .anyRequest().permitAll()
            }
            .formLogin {
                authenticationSuccessHandler()
            }

            .logout { logout ->
                logout
                    .deleteCookies("JSESSIONID")
                    .addLogoutHandler(logoutHandler())
                    .logoutSuccessUrl("/")
            }
            .sessionManagement { session ->
                session
                    .sessionFixation().migrateSession()
            }
            .csrf { csrf ->
                csrf.disable()
            }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user: UserDetails = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun logoutHandler(): LogoutHandler {
        return LogoutHandler { request, response, authentication ->
            SecurityContextHolder.clearContext()
        }
    }

    @Bean
    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request, response, authentication ->
            response.sendRedirect("/manage")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<VRgalleryApplication>(*args)
}
