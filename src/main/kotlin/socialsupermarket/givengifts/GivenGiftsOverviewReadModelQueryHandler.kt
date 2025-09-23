package socialsupermarket.givengifts

import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.common.Event

@Component
class GivenGiftsOverviewReadModelQueryHandler(val eventStore: EventStore) {

    @QueryHandler
    fun handle(query: GivenGiftsOverviewQuery) : GivenGiftsOverviewReadModel {
        val events: List<Event> =  eventStore
            .readEvents(query.aggregateId.toString())
            .asStream()
            .filter { it.payload is Event }
            .map { it.payload as Event }
            .toList()

        return GivenGiftsOverviewReadModel().applyEvents(events)
    }
}