package socialsupermarket.members

import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import socialsupermarket.authentication.UserContextService

@Controller
class MemberController(private val queryGateway: QueryGateway,
                             private val userContextService: UserContextService) {

    @GetMapping("/members/search")
    @ResponseBody
    fun searchMembers(@RequestParam("memberName") searchTerm: String): String {


        val membersReadModel =
            queryGateway.query(AllMembersSearchQuery(searchTerm, userContextService.getCurrentUserEmail()), MembersReadModel::class.java)

        return buildString {
            membersReadModel.get().data.forEach { member ->
                append(
                    """
                        <div
                            class="w-full px-4 py-3 cursor-pointer hover:bg-gray-50 focus:bg-gray-50 transition-colors rounded-lg flex items-center gap-3 text-gray-800"
                            role="button"
                            tabindex="0"
                            data-member-id="${member.memberId}"
                            data-full-name="${member.firstName} ${member.lastName}"
                            onclick="selectMember(this.dataset.memberId, this.dataset.fullName)"
                            onkeydown="if(event.key==='Enter' || event.key===' ') { selectMember(this.dataset.memberId, this.dataset.fullName); event.preventDefault(); }"
                            aria-label="Select member ${member.firstName} ${member.lastName}"
                        >
                            <div class="w-8 h-8 bg-gradient-to-br from-nature-green to-green-600 rounded-full flex items-center justify-center text-white text-sm">👤</div>
                            <span class="font-medium">${member.firstName} ${member.lastName}</span>
                        </div>
                    """.trimIndent())

            }
        }
    }
}
