package socialsupermarket.events

import socialsupermarket.common.Event
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class SupportApprovedEvent(
    val fundingId: UUID,
    val requestId: UUID,
    val amount: Double,
    val approvalDate: LocalDate
): Event
