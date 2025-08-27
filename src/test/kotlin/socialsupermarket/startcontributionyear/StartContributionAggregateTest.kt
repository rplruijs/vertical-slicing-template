package socialsupermarket.startcontributionyear

import org.axonframework.test.aggregate.AggregateTestFixture

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import socialsupermarket.common.CommandException
import socialsupermarket.common.Event
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.domain.commands.contribution.StartContributionYearCommand
import socialsupermarket.domain.contribution.ContributionAggregate
import socialsupermarket.events.ContributionYearStartedEvent
import java.time.LocalDate
import java.util.UUID

class StartContributionAggregateTest {

    companion object {
        val CONTRIBUTION_ID: UUID = UUID.randomUUID()
        val MEMBER_ID: UUID = UUID.randomUUID()
        val START_DATE: LocalDate = LocalDate.now()
    }

    private lateinit var fixture: AggregateTestFixture<ContributionAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(ContributionAggregate::class.java)
    }

    @Test
    fun `start contribution year`() {

            //GIVEN
            val events = mutableListOf<Event>()

            //WHEN
            val command = StartContributionYearCommand(
                contributionId = CONTRIBUTION_ID,
                memberId = MEMBER_ID,
                startDate = START_DATE,

            )

            // THEN
            val expectedEvents = mutableListOf<Event>()

            val event = ContributionYearStartedEvent(
                contributionId = CONTRIBUTION_ID,
                memberId = MEMBER_ID,
                startDate = START_DATE,
            )

            expectedEvents.add(event)

            fixture
                .given(events)
                .`when`(command)
                .expectSuccessfulHandlerExecution()
                .expectEvents(*expectedEvents.toTypedArray())
    }
}