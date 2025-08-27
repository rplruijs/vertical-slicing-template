package socialsupermarket.supportapproved.integration

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAssserted
import socialsupermarket.domain.commands.funding.AssessSupportRequestCommand
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.supportsapproved.GetApprovedSupportsQuery
import socialsupermarket.supportsapproved.SupportApprovedReadModel
import socialsupermarket.supportsapproved.SupportApprovedReadModelEntity
import java.util.UUID

class SupportApprovedReadModelTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway
    @Autowired private lateinit var queryGateway: QueryGateway

    companion object {
        private val REQUEST_ID = UUID.randomUUID()
        private val CONTRIBUTION_ID = UUID.randomUUID()
        private val REQUESTED_BY = UUID.randomUUID()
        private val REQUESTED_FOR = UUID.randomUUID()
        private const val APPROVED_STATUS = "APPROVED"
        private const val RELATIONSHIP = "Friends"
        private const val MONTH = "August"
        private const val AMOUNT = 100.0
        private const val NOTES = "He needs a surgery"
        private  val FUNDING_ID = UUID.randomUUID()
    }

    @Test
    fun `get approved supports`() {

        // WHEN - Register Gift Command
        val registerGiftCommand = RegisterGiftCommand(FUNDING_ID, AMOUNT)
        commandGateway.sendAndWait<Any>(registerGiftCommand)


        //AND - Request Monthly Support Command
        val requestCommand = RequestSupportCommand(
            requestId = REQUEST_ID,
            contributionId = CONTRIBUTION_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES
        )
        commandGateway.sendAndWait<Any>(requestCommand)

        // AND - Approve the support request
        val approveCommand = AssessSupportRequestCommand(
            fundingId = FUNDING_ID,
            requestId = REQUEST_ID,
            amount = AMOUNT
        )
        commandGateway.sendAndWait<Any>(approveCommand)

        // THEN - Verify the approved support is in the read model
        val expectedEntity = SupportApprovedReadModelEntity().apply {
            requestId = REQUEST_ID
            contributionId = CONTRIBUTION_ID
            requestedBy = REQUESTED_BY
            requestedFor = REQUESTED_FOR
            month = MONTH
            amount = AMOUNT
            status = APPROVED_STATUS
        }
        val expectedReadModel = SupportApprovedReadModel(listOf(expectedEntity))

        awaitUntilAssserted {
            val actualReadModel = queryGateway.query(GetApprovedSupportsQuery(1), SupportApprovedReadModel::class.java)

            assertThat(actualReadModel.get()).isNotNull
            assertThat(actualReadModel.get()).isEqualTo(expectedReadModel)
        }
    }
}
