package socialsupermarket.registrategift

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import socialsupermarket.common.CommandException
import socialsupermarket.common.Event
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import socialsupermarket.domain.funding.FundingAggregate
import org.junit.jupiter.api.Test
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.events.GiftRegisteredEvent

class GiftRegistratedAggregateTest {

    private lateinit var fixture: FixtureConfiguration<FundingAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(FundingAggregate::class.java)
    }

    @Test
    fun positiveAmountAccepted() {
        //GIVEN
        val events = mutableListOf<Event>()

        //WHEN
        val command = RegisterGiftCommand(DEFAULT_FUNDING_ID, 1000.0)

        // THEN
        val expectedEvents = mutableListOf<Event>()

        expectedEvents.add(GiftRegisteredEvent(DEFAULT_FUNDING_ID, 1000.0))

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }


    @Test
    fun negativeAmountNotAccepted() {

        //GIVEN
        val events = mutableListOf<Event>()

        //WHEN
        val command = RegisterGiftCommand( DEFAULT_FUNDING_ID, -100.0)

        fixture
            .given(events)
            .`when`(command)
            .expectException(CommandException::class.java)
    }
}