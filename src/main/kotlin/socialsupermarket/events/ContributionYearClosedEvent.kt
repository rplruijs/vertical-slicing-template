package socialsupermarket.events

import socialsupermarket.common.Event
import java.time.LocalDate
import java.util.UUID

data class ContributionYearClosedEvent(val contributionId: UUID, val memberId: UUID, val closeDate: LocalDate) : Event