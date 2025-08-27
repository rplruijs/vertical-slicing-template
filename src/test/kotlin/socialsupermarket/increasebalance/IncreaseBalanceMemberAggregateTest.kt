package socialsupermarket.increasebalance

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import socialsupermarket.common.Event
import org.junit.jupiter.api.Test
import socialsupermarket.domain.commands.member.IncreaseBalanceCommand
import socialsupermarket.domain.member.MemberBalanceAggregate
import socialsupermarket.events.BalanceIncreasedEvent
import socialsupermarket.events.MemberImportedEvent
import java.time.LocalDate
import java.util.UUID

class IncreaseBalanceMemberAggregateTest {

    private lateinit var fixture: FixtureConfiguration<MemberBalanceAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(MemberBalanceAggregate::class.java)
    }

    @Test
    fun `balance increased`() {
        //GIVEN
        val memberId = UUID.randomUUID()
        val events = mutableListOf<Event>()

        val memberImportedEvent = MemberImportedEvent(
            memberId = memberId,
            email = "remcoruijsenaars@gmail.com",
            firstName = "remco",
            lastName = "ruijsenaars",
            birthDate = LocalDate.now(),
            "banaan",
            0.0
        )

        events.add(memberImportedEvent)

        //WHEN
        val command = IncreaseBalanceCommand(memberId, 100.0)

        // THEN
        val expectedEvents = mutableListOf<Event>()

        expectedEvents.add(BalanceIncreasedEvent(memberId, 100.0))

        fixture
            .given(events)
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(*expectedEvents.toTypedArray())
    }
}