package socialsupermarket.domain.contribution

import socialsupermarket.common.Event
import java.util.UUID

data class SupportGivenEvent(
    val contributionId: UUID,
    val requestId: UUID,
    val amount: Double,
    val requestedFor: UUID
) : Event
