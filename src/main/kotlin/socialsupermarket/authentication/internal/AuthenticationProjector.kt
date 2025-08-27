package socialsupermarket.authentication.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import socialsupermarket.authentication.AuthenticationEntity
import socialsupermarket.events.MemberImportedEvent
import java.time.LocalDateTime
import java.util.*

interface AuthenticationRepository: JpaRepository<AuthenticationEntity, UUID> {
    fun findByEmail(email: String): AuthenticationEntity?
}

@Component
@ProcessingGroup("authentication")
class AuthenticationProjector(
    val repository: AuthenticationRepository,
    val passwordEncoder: PasswordEncoder
) {

    @EventHandler
    fun on(event: MemberImportedEvent) {
        repository.save(
            AuthenticationEntity().apply {
                memberId = event.memberId
                email = event.email
                password = passwordEncoder.encode(event.password)
                accountCreated = LocalDateTime.now()
            }
        )
    }
}
