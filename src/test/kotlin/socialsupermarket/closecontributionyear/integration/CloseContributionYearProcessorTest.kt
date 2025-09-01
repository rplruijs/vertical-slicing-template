package socialsupermarket.closecontributionyear.integration

import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.StreamAssertions
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import socialsupermarket.events.ContributionYearClosedEvent
import java.time.LocalDate

import java.util.UUID

@TestPropertySource(properties = ["app.scheduling.close-contribution=*/1 * * * * *"])
class CloseContributionYearProcessorTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway

    @Autowired private lateinit var streamAssertions: StreamAssertions

    companion object {
        val CONTRIBUTION_ID = UUID.randomUUID()
        val MEMBER_ID = UUID.randomUUID()
    }


    @Test
    fun `close contribution year processor test`() {

        val command = StartContributionYearCommand(contributionId = CONTRIBUTION_ID,
            memberId = MEMBER_ID,
            startDate = LocalDate.now().minusYears(1)
        )
        commandGateway.sendAndWait<Any>(command)

        awaitUntilAsserted {
            streamAssertions.assertEvent(CONTRIBUTION_ID) {it is ContributionYearClosedEvent }
        }
    }
}