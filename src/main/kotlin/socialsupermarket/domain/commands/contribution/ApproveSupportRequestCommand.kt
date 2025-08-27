package socialsupermarket.domain.commands.contribution

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.util.UUID

data class ApproveSupportRequestCommand(
    @TargetAggregateIdentifier var contributionId: UUID,
    val requestId: UUID,
    val amount: Double
) : Command