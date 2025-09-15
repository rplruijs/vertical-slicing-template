package socialsupermarket.supportsapproved.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
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
        val existingEntity = repository.findById(event.requestId)
        if (existingEntity.isPresent) {
            val entity = existingEntity.get()

            entity.amount = event.amount
            entity.status = "APPROVED"
            repository.save(entity)
        }
    }

    @EventHandler
    fun on(event: SupportGivenEvent) {
        val existingEntity = repository.findById(event.requestId)
        if (existingEntity.isPresent) {
            val entity = existingEntity.get()
            entity.status = "GIVEN"
            repository.save(entity)
        }
    }
}
