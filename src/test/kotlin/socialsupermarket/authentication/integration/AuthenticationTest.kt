package socialsupermarket.authentication.integration

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import socialsupermarket.authentication.AuthenticationEntity
import socialsupermarket.authentication.GetAuthenticationByEmailQuery
import socialsupermarket.authentication.ValidateCredentialsQuery
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.commands.member.ImportMemberCommand
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

class AuthenticationTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway
    @Autowired private lateinit var queryGateway: QueryGateway
    @Autowired private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var testMemberId: UUID
    private lateinit var testEmail: String
    private lateinit var testFirstName: String
    private lateinit var testLastName: String
    private lateinit var testPassword: String
    private lateinit var testBirthDate: LocalDate

    @BeforeEach
    fun setup() {
        // Setup test member data
        testMemberId = UUID.randomUUID()
        testEmail = "${testMemberId}@example.com"
        testFirstName = "Test"
        testLastName = "User"
        testPassword = "password123"
        testBirthDate = LocalDate.parse("01-01-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        // Import the member before each test
        val command = ImportMemberCommand(
            testMemberId,
            testEmail,
            testFirstName,
            testLastName,
            testBirthDate,
            testPassword,
            0.0
        )

        commandGateway.sendAndWait<Any>(command)
    }

    @Test
    fun `get authentication by email`() {

        awaitUntilAsserted {
            val auth = queryGateway.query(GetAuthenticationByEmailQuery(testEmail), AuthenticationEntity::class.java).get()

            assertThat(auth).isNotNull
            assertThat(auth.memberId).isEqualTo(testMemberId)
            assertThat(auth.email).isEqualTo(testEmail)
            assertThat(passwordEncoder.matches(testPassword, auth.password)).isTrue()
            assertThat(auth.accountCreated).isNotNull
        }
    }


    @Test
    fun `validate credentials - valid`() {
        awaitUntilAsserted {
            val isValid = queryGateway.query(ValidateCredentialsQuery(testEmail, testPassword), Boolean::class.java).get()

            assertThat(isValid).isTrue
        }
    }

    @Test
    fun `validate credentials - invalid password`() {
        awaitUntilAsserted {
            val isValid = queryGateway.query(ValidateCredentialsQuery(testEmail, "wrongpassword"), Boolean::class.java).get()

            assertThat(isValid).isFalse
        }
    }

    @Test
    fun `validate credentials - invalid email`() {
        awaitUntilAsserted {
            val isValid = queryGateway.query(ValidateCredentialsQuery("nonexistent@example.com", testPassword), Boolean::class.java).get()

            assertThat(isValid).isFalse
        }
    }
}
