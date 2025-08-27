package socialsupermarket.authentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfig {

    /**
     * Creates a BCryptPasswordEncoder bean for secure password hashing.
     * BCryptPasswordEncoder automatically handles salting and is considered
     * secure for password storage.
     *
     * Using a strength of 12 provides a good balance between security and performance.
     * The default is 10, but increasing it makes brute force attacks more computationally expensive.
     *
     * @return A BCryptPasswordEncoder instance with strength 12
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }
}
