package socialsupermarket.read.daystats

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import java.time.LocalDate
import java.util.UUID

class DayStatsReadModelTest : BaseIntegrationTest() {

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
        val GIFT_DATE = LocalDate.of(2010, 10, 1)
        val REQUESTED_FOR_NAME = "Isa Ruijsenaars"
    }

    @Test
    fun `one gift`() {
        commandGateway.sendAndWait<Any>(RegisterGiftCommand(DEFAULT_FUNDING_ID, AMOUNT, GIFT_DATE))

        awaitUntilAsserted {
            val actualReadModel =
                queryGateway.query(AllDayStatsQuery(), AllDayStatsReadModel::class.java)

            assertThat(actualReadModel.get())
                .isEqualTo(
                    AllDayStatsReadModel(listOf(DayStatsReadModel(date = GIFT_DATE, requestsApproved = 0, giftsReceived = 1)))
                )
        }
    }



    @Test
    fun `one gift one request approved, different days`() {

    }
}
