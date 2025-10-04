package socialsupermarket.write.memberexported


import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import socialsupermarket.domain.commands.member.ImportMemberCommand

@Component
class MemberExportedKafkaConsumer(val commandGateway: CommandGateway, ) {
    var logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["member-initial-load"], groupId = "members-group")
    fun handle(event: MemberExportedEvent) {

        commandGateway.sendAndWait<ImportMemberCommand>(
            ImportMemberCommand(
                memberId = event.memberId,
                email = event.email,
                firstName = event.firstName,
                lastName = event.lastName,
                birthDate = event.birthDate,
                password = event.password,
                currentBalance = 0.0,
            )
        )

    }
}