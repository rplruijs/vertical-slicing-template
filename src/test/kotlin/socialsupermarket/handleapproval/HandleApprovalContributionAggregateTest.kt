package socialsupermarket.handleapproval

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import socialsupermarket.common.Event
import org.junit.jupiter.api.Test
import socialsupermarket.domain.commands.contribution.HandleApprovalCommand
import socialsupermarket.domain.contribution.ContributionAggregate
import socialsupermarket.events.SupportGivenEvent
import socialsupermarket.events.SupportRequestedEvent
import java.util.UUID

class HandleApprovalContributionAggregateTest {

    private lateinit var fixture: FixtureConfiguration<ContributionAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(ContributionAggregate::class.java)
    }

    @Test
    fun `handle approval`() {
        //GIVEN
        val contributionId = UUID.randomUUID()
        val requestId = UUID.randomUUID()
        val requestedBy = UUID.randomUUID()
        val requestedFor = UUID.randomUUID()
        val amount = 100.0

        val events = mutableListOf<Event>()
        events.add(SupportRequestedEvent(
            contributionId = contributionId,
            requestId = requestId,
            requestedBy = requestedBy,
            requestedFor = requestedFor,
            relationShip = "Friends",
            amount = amount,
            month = "August",
            notes = "He needs a surgery",
            requestedForName = "Alice Sly"
        ))

        //WHEN
        val command = HandleApprovalCommand(contributionId, requestId, requestedBy, requestedFor, "August", amount)

        // THEN
        val expectedEvents = mutableListOf<Event>()
        expectedEvents.add(SupportGivenEvent(contributionId, requestId, amount, requestedFor))

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }
}