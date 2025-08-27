package socialsupermarket.contributionstoclose.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.contributionstoclose.ContributionToCloseReadModelEntity
import socialsupermarket.events.ContributionYearClosedEvent
import socialsupermarket.events.ContributionYearStartedEvent
import java.time.LocalDate
import java.util.UUID

interface ContributionToCloseReadModelRepository: JpaRepository<ContributionToCloseReadModelEntity, UUID> {
    fun findByContributionId(contributionID: UUID): ContributionToCloseReadModelEntity?
    fun findByCloseDateLessThanEqualOrderByCloseDateAsc(closeDate: LocalDate): List<ContributionToCloseReadModelEntity>
}

@Component
class ContributionToCloseReadModelProjector(val repository: ContributionToCloseReadModelRepository) {
    @EventHandler
    fun on(event: ContributionYearStartedEvent) {
        repository.save<ContributionToCloseReadModelEntity>(
            ContributionToCloseReadModelEntity().apply {
                contributionId = event.contributionId
                memberId = event.memberId
                closeDate = event.startDate.plusYears(1)
            },
        )
    }

    @EventHandler
    fun on(event: ContributionYearClosedEvent) {
        repository.deleteById(event.contributionId)
    }
}