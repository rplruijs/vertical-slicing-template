package socialsupermarket.members.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.members.AllMembersQuery
import socialsupermarket.members.GetMemberQuery
import socialsupermarket.members.IsEmailAvailableQuery
import socialsupermarket.members.MemberReadModelEntity
import socialsupermarket.members.MembersReadModel
import java.util.concurrent.ConcurrentLinkedDeque

@Component
@ProcessingGroup("members")
class MemberReadModelQueryHandler(val repository: MemberReadModelRepository) {

    val limitedDeque = ConcurrentLinkedDeque<MemberReadModelEntity>()

    @QueryHandler
    fun handle(query: AllMembersQuery) : MembersReadModel {
        val members = repository.findAll()
        val set = members.toMutableSet()
        set.addAll(limitedDeque)
        return MembersReadModel(set.toList())
    }

    @QueryHandler
    fun handle(query: IsEmailAvailableQuery): Boolean {
        if (repository.findByEmail(query.email) != null) {
            return false
        }

        return limitedDeque.none { it.email == query.email }
    }


    @QueryHandler
    fun handle(query: GetMemberQuery) : MemberReadModelEntity? {
        return repository.findByMemberId(query.memberId)
    }

    @EventHandler
    fun on(event: MemberImportedEvent) {
        while (limitedDeque.size > 20) {
            limitedDeque.pollFirst()
        }
        val item =
            MemberReadModelEntity().apply {
                this.memberId = event.memberId
                this.email = event.email
                this.firstName = event.firstName
                this.lastName = event.lastName
                this.birthDate = event.birthDate
            }
        if (!limitedDeque.contains(item)) {
            limitedDeque.push(item)
        }
    }
}
