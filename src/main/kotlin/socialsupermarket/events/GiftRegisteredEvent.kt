package socialsupermarket.events

import socialsupermarket.common.Event
import java.util.UUID

data class GiftRegisteredEvent(val fundingId: UUID, val amount: Double) : Event