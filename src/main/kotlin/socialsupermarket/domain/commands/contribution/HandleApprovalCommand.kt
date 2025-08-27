package socialsupermarket.domain.commands.contribution

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.util.UUID

data class HandleApprovalCommand(
    @TargetAggregateIdentifier var contributionId: UUID,
    val requestId: UUID,
    val requestedBy: UUID,
    val requestedFor: UUID,
    val month: String,
    val amount: Double
): Command