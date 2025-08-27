package socialsupermarket.events

import socialsupermarket.common.Event
import java.util.UUID

data class SupportWaitForFundingEvent(
    val fundingId: UUID,
    val requestId: UUID,
    val amount: Double
) : Event

