package socialsupermarket.supportrequests.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.events.SupportRequestedEvent

import socialsupermarket.supportrequests.SupportRequestReadModelEntity
import java.util.UUID

interface SupportRequestReadModelRepository: JpaRepository<SupportRequestReadModelEntity, UUID>

@Component
class SupportRequestReadModelProjector(val repository: SupportRequestReadModelRepository) {
    @EventHandler
    fun on(event: SupportRequestedEvent) {
        repository.save<SupportRequestReadModelEntity>(
            SupportRequestReadModelEntity().apply {
                requestId = event.requestId
                contributionId = event.contributionId
                amount = event.amount
            },
        )
    }
}