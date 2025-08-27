package socialsupermarket.supportrequests

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAssserted
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import java.util.UUID

import kotlin.jvm.java

class SupportRequestsReadModelTest : BaseIntegrationTest() {
    @Autowired private lateinit var commandGateway: CommandGateway
    @Autowired private lateinit var queryGateway: QueryGateway

    companion object {
        private val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        private val REQUEST_ID = UUID.randomUUID()
        private val REQUESTED_BY = UUID.randomUUID()
        private val REQUESTED_FOR = UUID.randomUUID()
        private const val RELATIONSHIP = "Friends"
        private const val MONTH = "August"
        private const val AMOUNT = 100.0
        private const val NOTES = "He need a surgery"
    }


    @Test
    fun `get support request`() {
        //WHEN
        val command = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES
        )
        commandGateway.sendAndWait<Any>(command)

        //THEN
        val readModelEntity = SupportRequestReadModelEntity().apply {
            requestId = REQUEST_ID
            amount = AMOUNT
            contributionId = CONTRIBUTION_ID
        }
        val expectedReadModel = SupportRequestReadModel(readModelEntity)

        awaitUntilAssserted {
            val actualReadModel = queryGateway.query(GetSupportRequestQuery(REQUEST_ID), SupportRequestReadModel::class.java)

            assertThat(actualReadModel.get()).isNotNull
            assertThat(actualReadModel.get()).isEqualTo(expectedReadModel)
        }
    }

    @Test
    fun `get all support requests`() {
        //WHEN
        val command = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES
        )
        commandGateway.sendAndWait<Any>(command)

        //THEN
        val readModelEntity = SupportRequestReadModelEntity().apply {
            requestId = REQUEST_ID
            amount = AMOUNT
            contributionId = CONTRIBUTION_ID
        }
        val expectedReadModel = SupportRequestsReadModel(listOf(readModelEntity))

        awaitUntilAssserted {
            val actualReadModel = queryGateway.query(GetAllSupportRequests(), SupportRequestsReadModel::class.java)

            assertThat(actualReadModel.get()).isNotNull
            assertThat(actualReadModel.get()).isEqualTo(expectedReadModel)
        }
    }
}
