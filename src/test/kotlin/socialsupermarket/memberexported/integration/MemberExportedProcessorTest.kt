package socialsupermarket.memberexported.integration


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.StreamAssertions
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.write.memberexported.MemberExportedEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class MemberExportedProcessorTest : BaseIntegrationTest() {
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, MemberExportedEvent>

    @Autowired
    private lateinit var streamAssertions: StreamAssertions


    @Test
    fun `member imported from initial load processor test`() {
        val memberId = UUID.randomUUID()
        val email = "erik@gmail.com"
        val firstName = "Erik"
        val lastName = "Mertz"
        val password = "banaan"
        val birthDate = LocalDate.parse("09-03-1978", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val currentBalance = 0.0

        awaitUntilAsserted {
            kafkaTemplate.executeInTransaction {
                it
                    .send(
                        "member-initial-load",
                        MemberExportedEvent(memberId, email, firstName, lastName, birthDate, password, currentBalance)
                    )
                    .get()
            }

            val expectedEvent = MemberImportedEvent(memberId, email, firstName, lastName, birthDate, password, 0.0)

            streamAssertions.assertEvent(memberId) {
                it is MemberImportedEvent && it == expectedEvent
            }
        }
    }
}