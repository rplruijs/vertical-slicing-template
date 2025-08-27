package socialsupermarket.members.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.members.MemberReadModelEntity
import java.util.UUID

interface MemberReadModelRepository: JpaRepository<MemberReadModelEntity, UUID> {
    fun findByMemberId(memberId: UUID): MemberReadModelEntity?
    fun findByEmail(memberEmail: String): MemberReadModelEntity?
}

@Component
class MemberReadModelProjector(val repository: MemberReadModelRepository) {
    @EventHandler
    fun on(event: MemberImportedEvent) {
        repository.save<MemberReadModelEntity>(
            MemberReadModelEntity().apply {
                memberId = event.memberId
                email = event.email
                firstName = event.firstName
                lastName = event.lastName
                birthDate = event.birthDate
            },
        )
    }
}