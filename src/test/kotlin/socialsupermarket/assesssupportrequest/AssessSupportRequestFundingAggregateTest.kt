package socialsupermarket.assesssupportrequest

import org.axonframework.test.aggregate.AggregateTestFixture

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import socialsupermarket.common.Event
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.AssessSupportRequestCommand
import socialsupermarket.domain.funding.FundingAggregate
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.events.SupportWaitForFundingEvent
import java.time.LocalDate
import java.util.UUID

class AssessSupportRequestFundingAggregateTest {

    companion object {
        val REQUEST_ID: UUID = UUID.randomUUID()
    }

    private lateinit var fixture: AggregateTestFixture<FundingAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(FundingAggregate::class.java)
    }

    @Test
    fun `assess support request - fully approved`() {

            //GIVEN
            val events = mutableListOf<Event>()
            val giftRequestedEvents = GiftRegisteredEvent(DEFAULT_FUNDING_ID, 300.0, LocalDate.now())
            events.add(giftRequestedEvents)

            //WHEN
            val command = AssessSupportRequestCommand(
                fundingId = DEFAULT_FUNDING_ID,
                amount =  100.0,
                requestId = REQUEST_ID,
                assessDate = LocalDate.of(2024, 12, 31)
            )

            // THEN
            val expectedEvents = mutableListOf<Event>()
            val supportApprovedEvent = SupportApprovedEvent(
                fundingId = DEFAULT_FUNDING_ID,
                requestId = REQUEST_ID,
                amount = 100.0,
                approvalDate = LocalDate.of(2024, 12, 31)
            )
            expectedEvents.add(supportApprovedEvent)

            fixture
                .given(events)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(*expectedEvents.toTypedArray())
        }

    @Test
    fun `assess support request - partially approved`() {

        //GIVEN
        val events = mutableListOf<Event>()
        val giftRequestedEvents = GiftRegisteredEvent(DEFAULT_FUNDING_ID, 80.0, LocalDate.now())
        events.add(giftRequestedEvents)

        //WHEN
        val command = AssessSupportRequestCommand(
            fundingId = DEFAULT_FUNDING_ID,
            amount =  100.0,
            requestId = REQUEST_ID,
            assessDate = LocalDate.of(2024, 12, 31)
        )

        // THEN
        val expectedEvents = mutableListOf<Event>()
        val supportApprovedEvent = SupportApprovedEvent(
            fundingId = DEFAULT_FUNDING_ID,
            requestId = REQUEST_ID,
            amount = 80.0,
            approvalDate = LocalDate.of(2024, 12, 31)
        )

        val supportWaitingForFundingEvent = SupportWaitForFundingEvent(
            fundingId = DEFAULT_FUNDING_ID,
            requestId = REQUEST_ID,
            amount = 20.0,
            atWaitingList = LocalDate.of(2024, 12, 31)
        )
        expectedEvents.add(supportApprovedEvent)
        expectedEvents.add(supportWaitingForFundingEvent)


        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }

    @Test
    fun `assess support request - nothing approved`() {

        //GIVEN
        val events = mutableListOf<Event>()


        //WHEN
        val command = AssessSupportRequestCommand(
            fundingId = DEFAULT_FUNDING_ID,
            amount =  100.0,
            requestId = REQUEST_ID,
            assessDate = LocalDate.of(2024, 12, 31)
        )

        // THEN
        val expectedEvents = mutableListOf<Event>()

        val supportWaitingForFundingEvent = SupportWaitForFundingEvent(
            fundingId = DEFAULT_FUNDING_ID,
            requestId = REQUEST_ID,
            amount = 100.0,
            atWaitingList = LocalDate.of(2024, 12, 31)
        )

        expectedEvents.add(supportWaitingForFundingEvent)

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }

}


