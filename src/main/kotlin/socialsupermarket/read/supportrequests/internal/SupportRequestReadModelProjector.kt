package socialsupermarket.read.supportrequests.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.events.SupportApprovedAfterWaitingForFundingEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.events.SupportRequestedEvent
import socialsupermarket.events.SupportWaitForFundingEvent

import socialsupermarket.read.supportrequests.SupportRequestReadModelEntity
import java.util.UUID

interface SupportRequestReadModelRepository: JpaRepository<SupportRequestReadModelEntity, UUID> {
    fun findAllByWaitingForFundingIsTrue(): List<SupportRequestReadModelEntity>
}

@Component
@ProcessingGroup("support-requests")
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

    @EventHandler
    fun on(event: SupportWaitForFundingEvent) {
        repository.findById(event.requestId).ifPresent {
            it.waitingForFunding = true
            it.amount = event.amount
            repository.save(it)
        }
    }

    @EventHandler
    fun on(event: SupportApprovedEvent) {
        repository.findById(event.requestId).ifPresent {
            if (it.amount == event.amount) {
                repository.deleteById(event.requestId)
            }
        }
    }

    @EventHandler
    fun on(event: SupportApprovedAfterWaitingForFundingEvent) {
        repository.findById(event.requestId).ifPresent {
            repository.deleteById(event.requestId)
        }
    }

}