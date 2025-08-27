package socialsupermarket.contributionlookup.internal

import jakarta.persistence.MapsId
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.contributionlookup.ContributionLookUpReadModelEntity
import socialsupermarket.contributionstoclose.ContributionToCloseReadModelEntity
import socialsupermarket.events.ContributionYearClosedEvent
import socialsupermarket.events.ContributionYearStartedEvent
import java.time.LocalDate
import java.util.UUID

interface ContributionLookUpReadModelRepository: JpaRepository<ContributionLookUpReadModelEntity, UUID> {
    fun findByMemberId(memberId: UUID): ContributionLookUpReadModelEntity?
}

@Component
class ContributionLookUpReadModelProjector(val repository: ContributionLookUpReadModelRepository) {
    @EventHandler
    fun on(event: ContributionYearStartedEvent) {
        repository.save<ContributionLookUpReadModelEntity>(
            ContributionLookUpReadModelEntity().apply {
                memberId = event.memberId
                contributionId = event.contributionId

            },
        )
    }

    @EventHandler
    fun on(event: ContributionYearClosedEvent) {
        repository.deleteById(event.memberId)
    }
}