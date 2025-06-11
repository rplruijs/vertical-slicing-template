package dinner_invitation.vertical_slicing.common.support

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Component

@Component
class StreamAssertions(private val eventStore: EventStore) {

    fun assertEvent(aggregateId: String, predicate: (event: Any) -> Boolean) {
        assertThat(eventStore.readEvents(aggregateId).asStream().map { it.payload }.toList())
            .anyMatch(predicate)
    }

    fun assertNoEvent(aggregateId: String, predicate: (event: Any) -> Boolean) {
        assertThat(eventStore.readEvents(aggregateId).asStream().map { it.payload }.toList())
            .noneMatch(predicate)
    }
}
