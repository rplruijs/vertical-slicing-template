package socialsupermarket.events

import socialsupermarket.common.Event
import java.util.UUID

data class SupportApprovedEvent(
    val fundingId: UUID,
    val requestId: UUID,
    val amount: Double
): Event
