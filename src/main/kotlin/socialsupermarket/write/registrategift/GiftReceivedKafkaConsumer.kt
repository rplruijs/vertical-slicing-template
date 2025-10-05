package socialsupermarket.write.registrategift


import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.RegisterGiftCommand
import java.time.LocalDate

@Component
class GiftReceivedKafkaConsumer(val commandGateway: CommandGateway, ) {


    @KafkaListener(topics = ["gift-received"], groupId = "gifts")
    fun handle(event: GiftReceivedEvent) {

        commandGateway.sendAndWait<RegisterGiftCommand>(
            RegisterGiftCommand(
                fundingId = DEFAULT_FUNDING_ID,
                amount = event.amount,
                date = LocalDate.now(),
                )
        )
    }
}