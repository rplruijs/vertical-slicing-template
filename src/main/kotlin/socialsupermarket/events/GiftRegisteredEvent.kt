package socialsupermarket.events

import socialsupermarket.common.Event
import java.time.LocalDate
import java.util.UUID

data class GiftRegisteredEvent(val fundingId: UUID, val amount: Double, val date: LocalDate) : Event