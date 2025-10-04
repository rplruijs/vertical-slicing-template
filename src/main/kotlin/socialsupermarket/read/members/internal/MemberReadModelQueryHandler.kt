package socialsupermarket.read.members.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.events.MemberImportedEvent
import socialsupermarket.read.members.AllMembersQuery
import socialsupermarket.read.members.AllMembersSearchQuery
import socialsupermarket.read.members.GetMemberQuery
import socialsupermarket.read.members.GetMemberWithEmail
import socialsupermarket.read.members.IsEmailAvailableQuery
import socialsupermarket.read.members.MemberReadModelEntity
import socialsupermarket.read.members.MembersReadModel
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
    fun handle(query: AllMembersSearchQuery) : MembersReadModel {
        val term = query.searchString.trim()
        if (term.isEmpty()) {
            return MembersReadModel(listOf())
        }
        return MembersReadModel(repository.searchByName(term, query.excludeMail))
    }

    @QueryHandler
    fun handle(query: GetMemberWithEmail) : MemberReadModelEntity? {
        return repository.findByEmail(query.email)
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
