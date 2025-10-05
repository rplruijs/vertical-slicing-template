package socialsupermarket.authentication.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.queryhandling.QueryHandler
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import socialsupermarket.authentication.AuthenticationEntity
import socialsupermarket.authentication.GetAuthenticationByEmailQuery
import socialsupermarket.authentication.ValidateCredentialsQuery

@Component
@ProcessingGroup("authentication")
class AuthenticationQueryHandler(
    val repository: AuthenticationRepository,
    val passwordEncoder: PasswordEncoder
) {

    @QueryHandler
    fun handle(query: ValidateCredentialsQuery): Boolean {
        val auth = repository.findByEmail(query.email) ?: return false
        return passwordEncoder.matches(query.password, auth.password)
    }

    @QueryHandler
    fun handle(query: GetAuthenticationByEmailQuery): AuthenticationEntity? {
        return repository.findByEmail(query.email)
    }


}
