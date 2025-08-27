package socialsupermarket.domain.member

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Aggregate
import socialsupermarket.common.CommandException
import socialsupermarket.domain.commands.member.CreateMemberAccountCommand
import socialsupermarket.domain.commands.member.ImportMemberCommand
import socialsupermarket.domain.commands.member.IncreaseBalanceCommand
import socialsupermarket.events.BalanceIncreasedEvent
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.members.IsEmailAvailableQuery
import java.util.UUID

@Aggregate
class MemberBalanceAggregate() {

    @AggregateIdentifier var memberId: UUID? = null
    var balance: Double = 0.0

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: ImportMemberCommand, queryGateway: QueryGateway) {

        queryGateway.query(IsEmailAvailableQuery(command.email), Boolean::class.java)
            .thenAccept { emailIsAvailable ->
                if (emailIsAvailable) {
                    AggregateLifecycle.apply(
                        MemberImportedEvent(
                            memberId = command.memberId,
                            email = command.email,
                            firstName = command.firstName,
                            lastName = command.lastName,
                            birthDate = command.birthDate,
                            password = command.password,
                            currentBalance = command.currentBalance,
                        )
                    )
            } else {
                throw CommandException(listOf("Email address already exists"))
            }
        }
    }

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: CreateMemberAccountCommand, queryGateway: QueryGateway) {
        val emailIsAvailable = queryGateway
            .query(IsEmailAvailableQuery(command.email), Boolean::class.java)
            .join() // Or .get(), but .join() throws unchecked exceptions

        if (emailIsAvailable) {
            AggregateLifecycle.apply(
                MemberImportedEvent(
                    memberId = command.memberId,
                    email = command.email,
                    firstName = command.firstName,
                    lastName = command.lastName,
                    birthDate = command.birthDate,
                    password = command.password,
                    currentBalance = 0.0,
                )
            )
        } else {
            throw CommandException(listOf("Email address already exists"))
        }
    }


    @CommandHandler
    fun handle(command: IncreaseBalanceCommand) {
        AggregateLifecycle.apply(
            BalanceIncreasedEvent(
                memberId = command.memberId,
                amount = command.amount
            )
        )
    }

    @EventSourcingHandler
    fun on(event: MemberImportedEvent) {
        memberId = event.memberId
    }

    @EventSourcingHandler
    fun on (event: BalanceIncreasedEvent) {
        balance += event.amount
    }
}