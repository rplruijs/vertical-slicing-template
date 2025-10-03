package socialsupermarket.domain.funding

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import socialsupermarket.common.CommandException
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.AssessSupportRequestCommand
import socialsupermarket.domain.commands.funding.AssessWaitingSupportRequestCommand
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.events.SupportApprovedAfterWaitingForFundingEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.events.SupportWaitForFundingEvent
import java.time.LocalDate
import java.util.UUID

@Aggregate
class FundingAggregate() {

    private var balance: Double = 0.0

    @AggregateIdentifier
    var fundingId: UUID? = null

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: RegisterGiftCommand) {
        if (command.amount < 0.0) {
            throw CommandException(listOf("Amount must be positive"))
        }
        AggregateLifecycle.apply(GiftRegisteredEvent(command.fundingId , command.amount))
    }

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: AssessWaitingSupportRequestCommand) {
        when {
            command.amount <= balance -> {
                AggregateLifecycle.apply(
                    SupportApprovedAfterWaitingForFundingEvent(
                        fundingId = command.fundingId,
                        requestId = command.requestId,
                        amount = command.amount,
                        approvalDate = command.assessDate,
                    )
                )
            }
        }
    }

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: AssessSupportRequestCommand) {
        when {
            command.amount <= balance -> {
                AggregateLifecycle.apply(
                    SupportApprovedEvent(
                        fundingId = command.fundingId,
                        requestId = command.requestId,
                        amount = command.amount,
                        approvalDate = command.assessDate,
                    )
                )

            }
            balance == 0.0 -> {
                AggregateLifecycle.apply(
                    SupportWaitForFundingEvent(
                        fundingId = command.fundingId,
                        requestId = command.requestId,
                        amount = command.amount,
                        atWaitingList = LocalDate.of(2024, 12, 31)
                    )
                )
            }
            else -> {
                val amountAwaitingFunding = command.amount - balance
                AggregateLifecycle.apply(
                    SupportApprovedEvent(
                        fundingId = command.fundingId,
                        requestId = command.requestId,
                        amount = command.amount - amountAwaitingFunding,
                        approvalDate = command.assessDate,
                    )
                ).andThenApply {
                    SupportWaitForFundingEvent(
                        fundingId = command.fundingId,
                        requestId = command.requestId,
                        amount = amountAwaitingFunding,
                        atWaitingList = command.assessDate
                    )
                }
            }
        }
    }

    @EventSourcingHandler
    fun on(event: GiftRegisteredEvent) {
        fundingId = DEFAULT_FUNDING_ID
        balance += event.amount

    }

    @EventSourcingHandler
    fun on(event: SupportApprovedEvent) {
        fundingId = DEFAULT_FUNDING_ID
        balance -= event.amount
    }

    @EventSourcingHandler
    fun on (event:SupportWaitForFundingEvent) {
        fundingId = event.fundingId
    }
}
