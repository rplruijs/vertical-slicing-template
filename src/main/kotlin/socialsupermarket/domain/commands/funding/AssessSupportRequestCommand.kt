package socialsupermarket.domain.commands.funding

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.util.UUID

data class AssessSupportRequestCommand(
    @TargetAggregateIdentifier var fundingId: UUID,
    val amount: Double,
    val requestId: UUID
) : Command