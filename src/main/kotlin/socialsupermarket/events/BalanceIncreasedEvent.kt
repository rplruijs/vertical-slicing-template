package socialsupermarket.events

import socialsupermarket.common.Event
import java.util.UUID

data class BalanceIncreasedEvent(val memberId: UUID, val amount: Double) : Event