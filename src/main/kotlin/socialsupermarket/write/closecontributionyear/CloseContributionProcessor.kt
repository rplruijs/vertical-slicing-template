package socialsupermarket.write.closecontributionyear

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway

import org.springframework.stereotype.Component
import socialsupermarket.common.Processor
import socialsupermarket.read.contributionstoclose.ContributionsToCloseReadModel
import socialsupermarket.read.contributionstoclose.GetContributionsToClose
import socialsupermarket.domain.commands.contribution.CloseContributionYearCommand
import java.time.LocalDate

@Component
class CloseContributionProcessor(
    val commandGateway: CommandGateway,
    val queryGateway: QueryGateway
) : Processor {


    fun closeContribution() {
        queryGateway.query(
            GetContributionsToClose(LocalDate.now()),
            ContributionsToCloseReadModel::class.java
        ).thenAccept { readModel ->

            if (readModel != null && readModel.data.isNotEmpty()) {
                readModel.data.forEach { contribution ->

                    val command = CloseContributionYearCommand(
                        contributionId = contribution.contributionId,
                        memberId = contribution.memberId,
                        closeDate = contribution.closeDate
                    )

                    commandGateway.send<CloseContributionYearCommand>(command)
                }
            }
        }
    }
}