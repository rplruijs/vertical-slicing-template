package socialsupermarket.requestsupport

import org.axonframework.test.aggregate.AggregateTestFixture

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import socialsupermarket.common.CommandException
import socialsupermarket.common.Event
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.contribution.ContributionAggregate
import socialsupermarket.events.SupportRequestedEvent
import java.util.UUID

class RequestSupportContributionAggregateTest {

    companion object {
        val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        val REQUEST_ID: UUID = UUID.randomUUID()
        val REQUEST_ID_2: UUID = UUID.randomUUID()
        val REQUESTED_BY: UUID = UUID.randomUUID()
        val REQUESTED_FOR: UUID = UUID.randomUUID()
    }

    private lateinit var fixture: AggregateTestFixture<ContributionAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(ContributionAggregate::class.java)
    }

    @Test
    fun `request support`() {

            //GIVEN
            val events = mutableListOf<Event>()

            //WHEN
            val command = RequestSupportCommand(
                contributionId = CONTRIBUTION_ID,
                requestId = REQUEST_ID,
                requestedBy = REQUESTED_BY,
                requestedFor = REQUESTED_FOR,
                relationShip = "Friends",
                month = "August",
                amount = 100.0,
                notes = "He needs a surgery"
            )

            // THEN
            val expectedEvents = mutableListOf<Event>()

            val event = SupportRequestedEvent(
                contributionId = CONTRIBUTION_ID,
                requestId = REQUEST_ID,
                requestedBy = REQUESTED_BY,
                requestedFor = REQUESTED_FOR,
                relationShip = "Friends",
                month = "August",
                amount = 100.0,
                notes = "He needs a surgery"
            )

            expectedEvents.add(event)

            fixture
                .given(events)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(*expectedEvents.toTypedArray())
    }

    @Test
    fun `maximum total support request per month is 100`() {
        //GIVEN
        val events = mutableListOf<Event>()

        val event = SupportRequestedEvent(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = UUID.randomUUID(),
            relationShip = "Friends",
            month = "August",
            amount = 100.0,
            notes = "He needs a surgery"
        )
        events.add(event)

        //WHEN
        val command = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID_2,
            requestedBy = REQUESTED_BY,
            requestedFor = UUID.randomUUID(),
            relationShip = "Neighbour",
            month = "August",
            amount = 20.0,
            notes = "She deserves a holiday"
        )

        fixture
            .given(events)
            .`when`(command)
            .expectException(CommandException::class.java)

    }

    @Test
    fun `request maximum amount of 100 for different months for the different members`() {
        //GIVEN
        val events = mutableListOf<Event>()

        val event = SupportRequestedEvent(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID,
            requestedBy = REQUESTED_BY,
            requestedFor = UUID.randomUUID(),
            relationShip = "Friends",
            month = "August",
            amount = 100.0,
            notes = "He needs a surgery"
        )
        events.add(event)

        //WHEN
        val command = RequestSupportCommand(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID_2,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = "Neighbour",
            month = "September",
            amount = 80.0,
            notes = "She deserves a holiday"
        )

        //THEN
        val expectedEvents = mutableListOf<Event>()

        val expectedEvent = SupportRequestedEvent(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID_2,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = "Neighbour",
            month = "September",
            amount = 80.0,
            notes = "She deserves a holiday"
        )
        expectedEvents.add(expectedEvent)

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }
}