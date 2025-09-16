package socialsupermarket.requestsupport

import org.axonframework.test.aggregate.AggregateTestFixture

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import socialsupermarket.common.CommandException
import socialsupermarket.common.Event
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.contribution.ContributionAggregate
import socialsupermarket.events.SupportRequestedEvent
import java.time.LocalDate
import java.util.UUID

class RequestSupportContributionAggregateTest {

    companion object {
        val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        val REQUEST_ID: UUID = UUID.randomUUID()
        val REQUEST_ID_2: UUID = UUID.randomUUID()
        val REQUESTED_BY: UUID = UUID.randomUUID()
        val REQUESTED_FOR: UUID = UUID.randomUUID()
        val REQUESTED_FOR_NAME: String = "Alice Sly"
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
                notes = "He needs a surgery",
                requestedForName = REQUESTED_FOR_NAME,
                requestDate = LocalDate.of(2024, 12, 31),
            )

            // THEN
            val expectedEvents = mutableListOf<Event>()

            val event = SupportRequestedEvent(
                contributionId = CONTRIBUTION_ID,
                requestId = REQUEST_ID,
                requestedBy = REQUESTED_BY,
                requestedFor = REQUESTED_FOR,
                relationShip = "Friends",
                amount = 100.0,
                month = "August",
                notes = "He needs a surgery",
                requestedForName = REQUESTED_FOR_NAME,
                requestDate = LocalDate.of(2024, 12, 31),
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
            amount = 100.0,
            month = "August",
            notes = "He needs a surgery",
            requestedForName = REQUESTED_FOR_NAME,
            requestDate = LocalDate.of(2024, 12, 31),
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
            notes = "She deserves a holiday",
            requestedForName = REQUESTED_FOR_NAME,
            requestDate = LocalDate.of(2024, 12, 31),
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
            amount = 100.0,
            month = "August",
            notes = "He needs a surgery",
            requestedForName = REQUESTED_FOR_NAME,
            requestDate = LocalDate.of(2024, 12, 31),
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
            notes = "She deserves a holiday",
            requestedForName = REQUESTED_FOR_NAME,
            requestDate = LocalDate.of(2024, 12, 31),
        )

        //THEN
        val expectedEvents = mutableListOf<Event>()

        val expectedEvent = SupportRequestedEvent(
            contributionId = CONTRIBUTION_ID,
            requestId = REQUEST_ID_2,
            requestedBy = REQUESTED_BY,
            requestedFor = REQUESTED_FOR,
            relationShip = "Neighbour",
            amount = 80.0,
            month = "September",
            notes = "She deserves a holiday",
            requestedForName = REQUESTED_FOR_NAME,
            requestDate = LocalDate.of(2024, 12, 31),
        )
        expectedEvents.add(expectedEvent)

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }
}