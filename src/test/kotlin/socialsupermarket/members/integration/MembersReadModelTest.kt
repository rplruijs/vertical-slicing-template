package socialsupermarket.members.integration

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
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

    private lateinit var testFirstName: String
    private lateinit var testLastName: String
    private lateinit var testPassword: String
    private lateinit var testBirthDate: LocalDate
    private var testInitialBalance: Double = 0.0



    @Test
    fun `get all members`() {

        //WHEN
        val memberId = UUID.randomUUID()
        val email = "jesse@gmail.com"
        importMember(memberId, email)
        //THEN
        val expectedReadModel = MembersReadModel(listOf(createExpectedMember(memberId, email)))
        awaitUntilAsserted {

            val actualReadModel = queryGateway.query(AllMembersQuery(), MembersReadModel::class.java)

            assertThat(actualReadModel.get()).isNotNull
            assertThat(actualReadModel.get()).isEqualTo(expectedReadModel)
        }
    }


    @Test
    fun `get member`() {
        //WHEN
        val memberId = UUID.randomUUID()
        val email = "siem@gmail.com"
        importMember(memberId, email)
        val expectedMember = createExpectedMember(memberId, email)
        awaitUntilAsserted {
            val member = queryGateway.query(GetMemberQuery(memberId), MemberReadModelEntity::class.java).get()
            assertThat(member).isEqualTo(expectedMember)
        }
    }

    private fun importMember(memberId: UUID, email: String) {
        // Setup test member data
        testFirstName = "Jesse"
        testLastName = "Vogel"
        testPassword = "banaan"
        testBirthDate = LocalDate.parse("20-03-1978", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        testInitialBalance = 70.0

        // Import the member before each test
        val command = ImportMemberCommand(
            memberId,
            email,
            testFirstName,
            testLastName,
            testBirthDate,
            testPassword,
            0.0
        )

        commandGateway.sendAndWait<Any>(command)
    }

    private fun createExpectedMember(memberId: UUID, email: String) = MemberReadModelEntity().apply {
        this.memberId = memberId
        this.email = email
        this.firstName = testFirstName
        this.lastName = testLastName
        this.birthDate = testBirthDate
    }
}