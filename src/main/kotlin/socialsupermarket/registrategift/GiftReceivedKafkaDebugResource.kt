package socialsupermarket.registrategift

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
class GiftReceivedKafkaDebugResource(
    private var kafkaTemplate: KafkaTemplate<String, in GiftReceivedEvent>,
) {

    @PostMapping("/fake/external/gift-received-event")
    fun giftReceived(
        @RequestParam amount: Double,

    ) {
        kafkaTemplate.executeInTransaction {
            it
                .send("gift-received", GiftReceivedEvent(amount))
                .get()
        }
    }
}
