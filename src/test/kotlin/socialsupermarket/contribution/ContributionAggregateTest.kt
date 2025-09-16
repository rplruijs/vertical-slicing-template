package socialsupermarket.contribution

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.contribution.ContributionAggregate
import socialsupermarket.events.SupportRequestedEvent
import java.util.UUID

class ContributionAggregateTest {

    private lateinit var fixture: FixtureConfiguration<ContributionAggregate>

    companion object {
        private val REQUEST_ID = UUID.randomUUID()
        private val REQUESTED_BY = UUID.randomUUID()
        private val REQUESTED_FOR = UUID.randomUUID()
        private const val REQUESTED_FOR_NAME = "Helen Hofstede"
        private val CONTRIBUTION_ID = UUID.randomUUID()
        private const val RELATIONSHIP = "Friends"
        private const val MONTH = "August"
        private const val AMOUNT = 100.0
        private const val NOTES = "He need a surgery"
    }

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(ContributionAggregate::class.java)
    }

    @Test
    fun shouldCreateMonthlySupportRequestedEventWhenCommandIsValid() {
        // GIVEN - no previous events

        // WHEN
        val command = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            requestedForName = REQUESTED_FOR_NAME,
            relationShip = RELATIONSHIP,
            month = MONTH,
            amount = AMOUNT,
            notes = NOTES,
        )

        // THEN
        val expectedEvent = SupportRequestedEvent(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = RELATIONSHIP,
            amount = AMOUNT,
            month = MONTH,
            notes = NOTES,
            requestedForName = REQUESTED_FOR_NAME,
        )

        fixture.givenNoPriorActivity()
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(expectedEvent)
    }
}