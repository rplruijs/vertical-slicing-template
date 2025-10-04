package socialsupermarket.read.members.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.read.members.MemberReadModelEntity
import java.util.UUID

interface MemberReadModelRepository: JpaRepository<MemberReadModelEntity, UUID> {
    fun findByMemberId(memberId: UUID): MemberReadModelEntity?
    fun findByEmail(memberEmail: String): MemberReadModelEntity?

    // Returns all members whose firstName, lastName, or their combination contains the search term (case-insensitive)
    @Query(
        """
    SELECT m FROM MemberReadModelEntity m
    WHERE (
        LOWER(m.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(CONCAT(m.firstName, ' ', m.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(CONCAT(m.lastName, ' ', m.firstName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    )
    AND (:exceptEmail IS NULL OR LOWER(m.email) <> LOWER(:exceptEmail))
    """
    )
    fun searchByName(
        @Param("searchTerm") searchTerm: String,
        @Param("exceptEmail") exceptMemberWithMail: String?
    ): List<MemberReadModelEntity>
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