package socialsupermarket.domain.contribution

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import socialsupermarket.common.CommandException
import socialsupermarket.common.Month
import socialsupermarket.domain.commands.contribution.CloseContributionYearCommand
import socialsupermarket.domain.commands.contribution.HandleApprovalCommand
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import socialsupermarket.events.ContributionYearClosedEvent
import socialsupermarket.events.ContributionYearStartedEvent
import socialsupermarket.events.SupportGivenEvent
import socialsupermarket.events.SupportRequestedEvent
import java.util.UUID

@Aggregate
class ContributionAggregate() {

    @AggregateIdentifier
    var contributionId: UUID? = null

    val supportsRequested: MutableMap<UUID, Support> = mutableMapOf()

    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    @CommandHandler
    fun handle(command: StartContributionYearCommand) {
        AggregateLifecycle.apply(ContributionYearStartedEvent(
            contributionId = command.contributionId,
            memberId = command.memberId,
            startDate = command.startDate,
        ))
    }

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING) // TODO check if this is still necessary
    @CommandHandler
    fun handle(command: RequestSupportCommand) {

        val errorMessages: MutableList<String> = mutableListOf()

        if (maximumExceeded(command)) {
            errorMessages.add("Maximum request exceeded for contribution")
        }

        if (errorMessages.isNotEmpty()) {
            throw CommandException(errorMessages)
        }

        AggregateLifecycle.apply(SupportRequestedEvent(
            command.contributionId,
            command.requestId,
            command.requestedBy,
            command.requestedFor,
            command.relationShip,
            command.amount,
            command.month,
            command.notes,
        ))
    }

    @CommandHandler
    fun handle(command: CloseContributionYearCommand) {
        AggregateLifecycle.apply(ContributionYearClosedEvent(command.contributionId, command.memberId, command.closeDate))
    }

    private fun maximumExceeded(command: RequestSupportCommand): Boolean {
        val totalAmountRequested = supportsRequested.values
            .groupBy { it.month }
            .flatMap{ it.value }
            .filter { it.month == command.month }
            .sumOf { it.amountRequested }
        return totalAmountRequested >= 100.0
    }



    @CommandHandler
    fun handle(command: HandleApprovalCommand) {
        AggregateLifecycle.apply(
            SupportGivenEvent(
                contributionId = command.contributionId,
                requestId = command.requestId,
                amount = command.amount,
                requestedFor = command.requestedFor
            )
        )
    }


    @EventSourcingHandler
    fun handle(event: ContributionYearStartedEvent) {
        this.contributionId = event.contributionId
    }

    @EventSourcingHandler
    fun on(event: SupportRequestedEvent) {
        this.contributionId = event.contributionId
        this.supportsRequested[event.requestId] = Support(
            requestedFor = event.requestedFor,
            amountRequested = event.amount,
            month = event.month,
            )
    }
    @EventSourcingHandler
    fun on(event: SupportGivenEvent) {
        this.supportsRequested.computeIfPresent(event.requestId) { _, support ->
            support.copy(amountGiven = event.amount)
        }
    }

    data class Support(
        val requestedFor: UUID,
        val amountRequested: Double,
        val amountGiven: Double = 0.0,
        val month: Month,
    )
}

