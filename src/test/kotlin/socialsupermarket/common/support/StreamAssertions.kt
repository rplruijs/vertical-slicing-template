package socialsupermarket.common.support

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Component
import socialsupermarket.common.Event
import java.util.UUID

@Component
class StreamAssertions(private val eventStore: EventStore) {

    fun assertEvent(aggregateId: UUID, predicate: (event: Any) -> Boolean) {
        assertThat(eventStore.readEvents(aggregateId.toString()).asStream().map { it.payload }.toList())
            .anyMatch(predicate)
    }
}
