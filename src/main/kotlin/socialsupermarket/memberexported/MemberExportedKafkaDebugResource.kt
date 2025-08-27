package socialsupermarket.memberexported

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
class MemberExportedKafkaDebugResource(
    private var kafkaTemplate: KafkaTemplate<String, in MemberExportedEvent>,
) {

    @PostMapping("/fake/external/import-member")
    fun importMember(
        @RequestParam friendId: UUID,
        @RequestParam email: String,
        @RequestParam firstName: String,
        @RequestParam lastName: String,
        @RequestParam password: String,
        @RequestParam birthDate: LocalDate,
    ) {
        kafkaTemplate.executeInTransaction {
            it
                .send("member-initial-load",
                    MemberExportedEvent(friendId, email, firstName, lastName, birthDate, password, 0.0)
                )
                .get()
        }
    }
}
