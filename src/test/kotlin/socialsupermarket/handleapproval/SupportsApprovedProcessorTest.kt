package socialsupermarket.handleapproval

import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.StreamAssertions
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.domain.contribution.SupportGivenEvent

import java.util.UUID

class SupportsApprovedProcessorTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway

    @Autowired private lateinit var streamAssertions: StreamAssertions

    companion object {

        private val CONTRIBUTION_ID = UUID.randomUUID()
        private val REQUEST_ID = UUID.randomUUID()
        private val REQUESTED_BY = UUID.randomUUID()
        private val REQUESTED_FOR = UUID.randomUUID()
        private const val RELATIONSHIP = "Friends"
        private const val MONTH = "August"
        private const val AMOUNT = 100.0
        private const val NOTES = "He needs a surgery"
    }

    @Test
    fun `Supports approved processor test`() {

        val registerGiftCommand = RegisterGiftCommand(DEFAULT_FUNDING_ID, 1000.0)

        commandGateway.sendAndWait<Any>(registerGiftCommand)

        val requestSupportCommand = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES
        )

        commandGateway.sendAndWait<Any>(requestSupportCommand)

        awaitUntilAsserted {
            streamAssertions.assertEvent(CONTRIBUTION_ID) {it is SupportGivenEvent}
        }
    }
}