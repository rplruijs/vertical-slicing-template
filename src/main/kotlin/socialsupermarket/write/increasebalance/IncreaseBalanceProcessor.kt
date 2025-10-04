package socialsupermarket.write.increasebalance

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import socialsupermarket.domain.commands.member.IncreaseBalanceCommand
import socialsupermarket.events.SupportGivenEvent

@Component
class IncreaseBalanceProcessor(val commandGateway: CommandGateway) {

    @EventHandler
    fun handle(event: SupportGivenEvent) {
        commandGateway.sendAndWait<IncreaseBalanceCommand>(
            IncreaseBalanceCommand(
                memberId = event.requestedFor,
                amount = event.amount,
            )
        )
    }


}