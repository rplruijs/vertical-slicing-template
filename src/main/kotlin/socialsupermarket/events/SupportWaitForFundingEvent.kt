package socialsupermarket.events

import socialsupermarket.common.Event
import java.time.LocalDate
import java.util.UUID

data class SupportWaitForFundingEvent(
    val fundingId: UUID,
    val requestId: UUID,
    val amount: Double,
    val atWaitingList: LocalDate
) : Event

