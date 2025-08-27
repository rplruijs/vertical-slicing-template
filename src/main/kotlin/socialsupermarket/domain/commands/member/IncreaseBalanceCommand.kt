package socialsupermarket.domain.commands.member

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.util.UUID

data class IncreaseBalanceCommand(@TargetAggregateIdentifier val memberId: UUID, val amount: Double) :
    Command
