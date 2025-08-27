package socialsupermarket.authentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import socialsupermarket.authentication.jwt.JwtAuthenticationFilter

/**
 * Configuration class for Spring Security.
 * Implements security best practices for web applications.
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(private val jwtAuthenticationFilter: JwtAuthenticationFilter) {

    /**
     * Configures the security filter chain with:
     * - CSRF protection
     * - Secure session management
     * - Secure cookie configuration
     * - Public access to static resources and login page
     * - Protected access to other resources
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Enable CSRF protection with cookie-based token repository
            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            }

            // Use stateless session management for JWT
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Configure which requests are allowed without authentication
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        AntPathRequestMatcher("/"),
                        AntPathRequestMatcher("/login"),
                        AntPathRequestMatcher("/index"),
                        AntPathRequestMatcher("/login-clean"),
                        AntPathRequestMatcher("/login-clean/**"),
                        // Remove personal-landings-page from permitAll
                        AntPathRequestMatcher("/login/**"),
                        AntPathRequestMatcher("/authentication/**"),
                        AntPathRequestMatcher("/registration"),
                        AntPathRequestMatcher("/registration/**"),
                        AntPathRequestMatcher("/createaccount/**"),
                        AntPathRequestMatcher("/css/**"),
                        AntPathRequestMatcher("/js/**"),
                        AntPathRequestMatcher("/images/**"),
                        AntPathRequestMatcher("/webjars/**"),
                        AntPathRequestMatcher("/error")
                    ).permitAll()
                    // Protect personal-landings-page
                    .requestMatchers(AntPathRequestMatcher("/personal-landings-page")).authenticated()
                    .anyRequest().authenticated()
            }

            // Configure form login
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/personal-landings-page", true)
                    .permitAll()
            }

            // Configure logout to also clear JWT cookie
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "jwt_token") // Add JWT cookie name
                    .permitAll()
            }

            // Add JWT filter before the standard authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//
//            // Configure headers for security
//            .headers { headers ->
//                headers
//                    // Disable CSP entirely to diagnose issues
//                    .contentSecurityPolicy { csp ->
//                        csp.policyDirectives("default-src 'self'; script-src 'unsafe-inline' 'unsafe-hashes' 'self' https://cdn.tailwindcss.com https://unpkg.com 'sha256-z14XX1budF93FfUHpV6CJToD/vVXjKQ8xLSdUMMt/iM='; style-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com;")
//                    }
//            }

        return http.build()
    }
}
