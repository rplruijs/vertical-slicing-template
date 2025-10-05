package socialsupermarket.registrategift.integration

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import socialsupermarket.common.support.BaseIntegrationTest
import socialsupermarket.common.support.StreamAssertions
import socialsupermarket.common.support.awaitUntilAsserted
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.write.registrategift.GiftReceivedEvent
import java.time.LocalDate

class GiftReceivedProcessorTest : BaseIntegrationTest() {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, GiftReceivedEvent>

    @Autowired
    private lateinit var streamAssertions: StreamAssertions

    @Test
    fun `gift imported from funding processor`() {

            awaitUntilAsserted { kafkaTemplate.executeInTransaction {
                it.send("gift-received", GiftReceivedEvent(100.0)).get()
            }
                val expectedInternalEvent = GiftRegisteredEvent(DEFAULT_FUNDING_ID, 100.0, LocalDate.now())

                streamAssertions.assertEvent(DEFAULT_FUNDING_ID) {
                    it is GiftRegisteredEvent && it == expectedInternalEvent
                }
            }
    }
}