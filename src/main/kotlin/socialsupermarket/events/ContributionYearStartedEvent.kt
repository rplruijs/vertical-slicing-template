package socialsupermarket.events

import socialsupermarket.common.Event
import java.time.LocalDate
import java.util.UUID

data class ContributionYearStartedEvent(val contributionId: UUID, val memberId: UUID, val startDate: LocalDate) : Event