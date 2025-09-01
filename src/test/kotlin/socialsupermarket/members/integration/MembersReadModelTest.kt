package socialsupermarket.members.integration

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.commands.member.ImportMemberCommand
import socialsupermarket.members.AllMembersQuery
import socialsupermarket.members.GetMemberQuery
import socialsupermarket.members.MemberReadModelEntity
import socialsupermarket.members.MembersReadModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.jvm.java

class MembersReadModelTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway
    @Autowired private lateinit var queryGateway: QueryGateway

    private lateinit var testMemberId: UUID
    private lateinit var testEmail: String
    private lateinit var testFirstName: String
    private lateinit var testLastName: String
    private lateinit var testPassword: String
    private lateinit var testBirthDate: LocalDate
    private var testInitialBalance: Double = 0.0

    @BeforeEach
    fun setup() {
        // Setup test member data
        testMemberId = UUID.randomUUID()
        testEmail = "jesse@gmail.com"
        testFirstName = "Jesse"
        testLastName = "Vogel"
        testPassword = "banaan"
        testBirthDate = LocalDate.parse("20-03-1978", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        testInitialBalance = 70.0

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
    fun `get all members`() {
        // WHEN-THEN (member already imported in setup)
        val expectedReadModel = MembersReadModel(listOf(createExpectedMember()))
        awaitUntilAsserted {

            val actualReadModel = queryGateway.query(AllMembersQuery(), MembersReadModel::class.java)

            assertThat(actualReadModel.get()).isNotNull
            assertThat(actualReadModel.get()).isEqualTo(expectedReadModel)
        }
    }


    @Test
    fun `get member`() {
        // WHEN-THEN (member already imported in setup)
        val expectedMember = createExpectedMember()
        awaitUntilAsserted {
            val member = queryGateway.query(GetMemberQuery(testMemberId), MemberReadModelEntity::class.java).get()
            assertThat(member).isEqualTo(expectedMember)
        }
    }

    private fun createExpectedMember() = MemberReadModelEntity().apply {
        this.memberId = testMemberId
        this.email = testEmail
        this.firstName = testFirstName
        this.lastName = testLastName
        this.birthDate = testBirthDate
    }
}