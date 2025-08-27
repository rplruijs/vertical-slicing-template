package socialsupermarket.events

import socialsupermarket.common.Event
import java.util.UUID

data class SupportRequestedEvent(
    val contributionId: UUID,
    val requestId: UUID,
    val requestedBy: UUID,
    val requestedFor: UUID,
    val relationShip: String,
    val amount: Double,
    val month: String,
    val notes: String,
): Event
