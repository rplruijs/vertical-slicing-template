package socialsupermarket.domain.commands.contribution

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.util.UUID

data class RequestSupportCommand(
    @TargetAggregateIdentifier var contributionId: UUID,
    val requestId: UUID,
    val requestedBy: UUID,
    val requestedFor: UUID,
    val relationShip: String,
    val month: String,
    val amount: Double,
    val notes: String
): Command