package socialsupermarket.authentication

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

// Query classes for authentication
data class GetAuthenticationByEmailQuery(val email: String)
data class GetAuthenticationByMemberIdQuery(val memberId: UUID)
data class ValidateCredentialsQuery(val email: String, val password: String)

@Entity
@Table(name = "user_credentials")
class AuthenticationEntity {

    @Column(name = "member_id")
    @Id
    lateinit var memberId: UUID

    @Column(name = "email", unique = true)
    lateinit var email: String

    @Column(name = "password")
    lateinit var password: String

    @Column(name = "account_created")
    lateinit var accountCreated: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthenticationEntity

        if (memberId != other.memberId) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (accountCreated != other.accountCreated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberId.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + accountCreated.hashCode()
        return result
    }
}