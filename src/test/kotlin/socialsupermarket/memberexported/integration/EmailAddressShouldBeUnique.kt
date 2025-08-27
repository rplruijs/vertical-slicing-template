package socialsupermarket.memberexported.integration

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import socialsupermarket.domain.member.MemberBalanceAggregate

class EmailAddressShouldBeUnique {

    private lateinit var fixture: FixtureConfiguration<MemberBalanceAggregate>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(MemberBalanceAggregate::class.java)
    }



}