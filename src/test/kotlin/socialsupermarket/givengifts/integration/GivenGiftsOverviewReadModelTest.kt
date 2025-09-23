package socialsupermarket.givengifts.integration

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import socialsupermarket.domain.commands.funding.AssessSupportRequestCommand
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.fundingbalance.CurrentBalanceQuery
import socialsupermarket.fundingbalance.CurrentBalanceReadModel
import socialsupermarket.givengifts.Gift
import socialsupermarket.givengifts.GivenGiftsOverviewQuery
import socialsupermarket.givengifts.GivenGiftsOverviewReadModel
import socialsupermarket.requestsupport.RequestSupportContributionAggregateTest.Companion.REQUESTED_FOR
import java.time.LocalDate
import java.util.UUID

class GivenGiftsOverviewReadModelTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var commandGateway: CommandGateway
    @Autowired
    private lateinit var queryGateway: QueryGateway

    companion object {
        val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        val MEMBER_ID: UUID = UUID.randomUUID()
        val REQUEST_ID: UUID = UUID.randomUUID()
        val REQUEST_BY: UUID = UUID.randomUUID()
        val REQUESTED_FOR: UUID = UUID.randomUUID()
        val RELATIONSHIP = "Family"
        val MONTH = "January"
        val AMOUNT = 80.0
        val NOTES = "Some extra's"
        val REQUEST_DATE = LocalDate.now()
        val REQUESTED_FOR_NAME = "Isa Ruijsenaars"
    }

    @Test
    fun `no gifts given`() {
        commandGateway.sendAndWait<Any>(RegisterGiftCommand(DEFAULT_FUNDING_ID, 100.0))
        commandGateway.sendAndWait<Any>(StartContributionYearCommand(contributionId = CONTRIBUTION_ID, memberId = MEMBER_ID, startDate = LocalDate.now()))

        awaitUntilAsserted {
            val actualReadModel =
                queryGateway.query(GivenGiftsOverviewQuery(CONTRIBUTION_ID), GivenGiftsOverviewReadModel::class.java)

            assertThat(actualReadModel.get())
                .isEqualTo(
                    GivenGiftsOverviewReadModel().apply {
                        givenTotalAmount = 0.0
                        givenApprovedAmount = 0.0
                        givenRequestedAmount = 0.0
                        givenGifts = mutableListOf()
                    }
                )
        }
    }


    @Test
    fun `1 gift given`() {
        commandGateway.sendAndWait<Any>(RegisterGiftCommand(DEFAULT_FUNDING_ID, 100.0))
        commandGateway.sendAndWait<Any>(StartContributionYearCommand(contributionId = CONTRIBUTION_ID, memberId = MEMBER_ID, startDate = LocalDate.now()))
        commandGateway.sendAndWait<Any>(RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUEST_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES,
            requestDate = REQUEST_DATE,
            requestedForName = REQUESTED_FOR_NAME
        )
        )

        awaitUntilAsserted {
            val actualReadModel =
                queryGateway.query(GivenGiftsOverviewQuery(CONTRIBUTION_ID), GivenGiftsOverviewReadModel::class.java)

            assertThat(actualReadModel.get())
                .isEqualTo(
                    GivenGiftsOverviewReadModel().apply {
                        givenTotalAmount = AMOUNT
                        givenApprovedAmount = AMOUNT
                        givenRequestedAmount = 0.0
                        givenGifts = mutableListOf(Gift(
                            requestId = REQUEST_ID,
                            date = REQUEST_DATE,
                            requestedForName = REQUESTED_FOR_NAME,
                            reason = NOTES,
                            amount = AMOUNT,
                            status = "APPROVED"
                        ))
                    }
                )
        }
    }

    @Test
    fun `1 gift given and 1 gift requested`() {
        //TODO
    }

}
