package socialsupermarket.fundingbalance.integration

import org.assertj.core.api.Assertions
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.read.fundingbalance.CurrentBalanceQuery
import socialsupermarket.read.fundingbalance.CurrentBalanceReadModel
import java.time.LocalDate

class FundingBalanceReadModelOnlyGiftTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var commandGateway: CommandGateway
    @Autowired
    private lateinit var queryGateway: QueryGateway


    @Test
    fun `gift of x - currentBalance = x`() {
        commandGateway.sendAndWait<Any>(RegisterGiftCommand(DEFAULT_FUNDING_ID, 100.0, LocalDate.now()))

        awaitUntilAsserted {
            val actualReadModel =
                queryGateway.query(CurrentBalanceQuery(), CurrentBalanceReadModel::class.java)

            Assertions.assertThat(actualReadModel.get()).isEqualTo(CurrentBalanceReadModel(100.0, 0.0))
        }
    }

}