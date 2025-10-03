package socialsupermarket.memberexported

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.Month
import java.util.UUID

@RestController
class MemberExportedKafkaDebugResource(
    private var kafkaTemplate: KafkaTemplate<String, in MemberExportedEvent>,
) {

    @PostMapping("/fake/external/import-member")
    fun importFakeMember(
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

    @GetMapping("/debug/import-test-members")
    fun importTestMembers() {
        println("importTestMembers")
        listOf(
            FakeMember(UUID.randomUUID(), "remco@gmail.com", "Remco", "Ruijsenaars", "_Banaan99!", LocalDate.of(1978, Month.MARCH, 18)),
            FakeMember(UUID.randomUUID(), "hans@gmail.com", "Hans", "Tieben", "_Banaan99!", LocalDate.of(1978, Month.MARCH, 18)),
            FakeMember(UUID.randomUUID(), "rob@gmail.com", "Rob", "Nobel", "_Banaan99!", LocalDate.of(1978, Month.MARCH, 18)),
            FakeMember(UUID.randomUUID(), "siem@gmail.com", "Siem", "Ruijsenaars", "_Banaan99!", LocalDate.of(1978, Month.MARCH, 18)),
            FakeMember(UUID.randomUUID(), "helen@gmail.com", "Helen", "Hofstede", "_Banaan99!", LocalDate.of(1978, Month.MARCH, 18))
        ).forEach {importFakeMember((it))}
    }

    private fun importFakeMember(fakeMember: FakeMember) {
        kafkaTemplate.executeInTransaction {
            it
                .send(
                    "member-initial-load",
                    MemberExportedEvent(fakeMember.friendId, fakeMember.email, fakeMember.firstName, fakeMember.lastName, LocalDate.of(1978, Month.MARCH, 18), fakeMember.password, 0.0)
                )
                .get()
        }
    }


    data class FakeMember (
        val friendId: UUID,
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val birthDate: LocalDate
    )

}
