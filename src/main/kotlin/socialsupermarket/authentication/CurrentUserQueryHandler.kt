package socialsupermarket.authentication

import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.read.members.GetCurrentMember
import socialsupermarket.read.members.GetMemberWithEmail
import socialsupermarket.read.members.MemberReadModelEntity

@Component
class CurrentUserQueryHandler(val userContextService: UserContextService,
                                    val queryGateway: QueryGateway) {

    @QueryHandler
    fun handle(query: GetCurrentMember): MemberReadModelEntity {
        val currentUserEmail =
            userContextService.getCurrentUserEmail() ?: throw IllegalStateException("No user is logged in")

        return queryGateway.query(GetMemberWithEmail(currentUserEmail), MemberReadModelEntity::class.java).join()
    }
}