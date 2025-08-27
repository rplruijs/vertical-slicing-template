package socialsupermarket.startcontributionyear

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.DisallowReplay
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import socialsupermarket.events.ContributionYearClosedEvent
import socialsupermarket.events.MemberImportedEvent
import java.time.LocalDate
import java.util.*

@DisallowReplay
@Component
@ProcessingGroup("start-contribution-year")
class StartContributionProcessor(var commandGateway: CommandGateway) {

    @EventHandler
    fun on(event: MemberImportedEvent) {
            commandGateway.send<StartContributionYearCommand>(StartContributionYearCommand(
                contributionId = UUID.randomUUID(),
                memberId = event.memberId,
                startDate = LocalDate.now(),
            ))
    }

    @EventHandler
    fun on (event: ContributionYearClosedEvent) {
        commandGateway.send<StartContributionYearCommand>(StartContributionYearCommand(
            contributionId = UUID.randomUUID(),
            memberId = event.memberId,
            startDate = event.closeDate,
        ))
    }
}