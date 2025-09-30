package socialsupermarket.assesssupportrequest

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.springframework.stereotype.Component
import org.axonframework.eventhandling.EventHandler
import socialsupermarket.common.Processor
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.AssessSupportRequestCommand
import socialsupermarket.events.SupportRequestedEvent
import java.time.LocalDate


@Component
@ProcessingGroup("support-requests")
class SupportRequestProcessor(val commandGateway: CommandGateway) : Processor {

    @EventHandler
    fun on(event: SupportRequestedEvent) {
        commandGateway.send<AssessSupportRequestCommand>(
            AssessSupportRequestCommand(
                fundingId = DEFAULT_FUNDING_ID,
                amount = event.amount,
                requestId = event.requestId,
                assessDate = LocalDate.now()
            )
        )
    }
}