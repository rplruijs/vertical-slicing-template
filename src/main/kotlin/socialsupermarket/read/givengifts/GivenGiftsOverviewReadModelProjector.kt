package socialsupermarket.read.givengifts

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component
import socialsupermarket.common.Event
import socialsupermarket.events.SupportGivenEvent
import socialsupermarket.events.SupportRequestedEvent

import java.util.UUID

@Component
@ProcessingGroup("given-gifts")
class GivenGiftsOverviewReadModelProjector(val queryUpdateEmitter: QueryUpdateEmitter,
                                                 val eventStore: EventStore) {

    @EventHandler
    fun on(event: SupportRequestedEvent) {
        emit(event.contributionId)
    }

    @EventHandler
    fun on(event: SupportGivenEvent) {
        emit(event.contributionId)
    }


    private fun emit(contributionId : UUID) {

        val events: List<Event> =  eventStore
            .readEvents(contributionId.toString())
            .asStream()
            .filter { it.payload is Event }
            .map { it.payload as Event }
            .toList()

        queryUpdateEmitter.emit(GivenGiftsOverviewQuery::class.java,
            {query -> query.aggregateId == contributionId},
            GivenGiftsOverviewReadModel().applyEvents(events))
    }
}