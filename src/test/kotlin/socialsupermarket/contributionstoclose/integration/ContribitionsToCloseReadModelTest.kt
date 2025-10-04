package socialsupermarket.contributionstoclose.integration

import org.assertj.core.api.Assertions
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.read.contributionstoclose.ContributionToCloseReadModelEntity
import socialsupermarket.read.contributionstoclose.ContributionsToCloseReadModel
import socialsupermarket.read.contributionstoclose.GetContributionToClose
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import java.time.LocalDate
import java.util.UUID

class ContribitionsToCloseReadModelTest : BaseIntegrationTest() {

    @Autowired private lateinit var commandGateway: CommandGateway
    @Autowired private lateinit var queryGateway: QueryGateway


    companion object {
        val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        val MEMBER_ID: UUID = UUID.randomUUID()
    }

    @Test
    fun `contribution read model test`() {

        commandGateway.sendAndWait<StartContributionYearCommand>(
                StartContributionYearCommand(
                    contributionId = CONTRIBUTION_ID,
                    memberId = MEMBER_ID,
                    startDate = LocalDate.parse("2025-01-01"),
                )
            )

        val expectedResult = ContributionsToCloseReadModel(listOf(
            ContributionToCloseReadModelEntity().apply {
                contributionId= CONTRIBUTION_ID
                memberId = MEMBER_ID
                closeDate = LocalDate.parse("2026-01-01")
            }
        ))

        awaitUntilAsserted {
            val result = queryGateway.query(GetContributionToClose(CONTRIBUTION_ID), ContributionsToCloseReadModel::class.java)

            Assertions.assertThat(result.get()).isNotNull
            Assertions.assertThat(result.get()).isEqualTo(expectedResult)
        }
    }

}