package socialsupermarket.supportsapproved.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.events.SupportApprovedAfterWaitingForFundingEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.events.SupportGivenEvent
import socialsupermarket.events.SupportRequestedEvent
import socialsupermarket.supportsapproved.SupportApprovedReadModelEntity
import java.util.UUID

interface SupportsApprovedReadModelRepository: JpaRepository<SupportApprovedReadModelEntity, UUID> {
    fun findByStatusIs(status: String, pageable: Pageable): List<SupportApprovedReadModelEntity>
}

@Component
class SupportsApprovedReadModelProjector(val repository: SupportsApprovedReadModelRepository) {
    @EventHandler
    fun on(event: SupportRequestedEvent) {
        val entity = SupportApprovedReadModelEntity().apply {
            requestId = event.requestId
            contributionId = event.contributionId
            requestedBy = event.requestedBy
            requestedFor = event.requestedFor
            month = event.month
            status = "REQUESTED"
        }
        repository.save(entity)
    }

    @EventHandler
    fun on(event: SupportApprovedEvent) {
        updateSupportRequest(event.requestId, event.amount, "APPROVED");
    }

    @EventHandler
    fun on (event: SupportApprovedAfterWaitingForFundingEvent) {
        updateSupportRequest(event.requestId, event.amount, "APPROVED_AFTER_WAITING")
    }

    @EventHandler
    fun on(event: SupportGivenEvent) {
        updateSupportRequest(event.requestId, event.amount, "GIVEN")
    }

    private fun updateSupportRequest(requestId: UUID, amount: Double, status: String) {
        val existingEntity = repository.findById(requestId)
        if (existingEntity.isPresent) {
            val entity = existingEntity.get()
            entity.amount = amount
            entity.status = status
            repository.save(entity)
        }
    }
}
