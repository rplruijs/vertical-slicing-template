package socialsupermarket.givengifts

import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import reactor.core.publisher.Flux
import socialsupermarket.common.HtmlSnippet
import socialsupermarket.contributionlookup.ContributionLookUpReadModelEntity
import socialsupermarket.contributionlookup.GetContribution

import socialsupermarket.members.GetCurrentMember
import socialsupermarket.members.MemberReadModelEntity

@Controller
class GivenGiftsOverviewController(
    private val queryGateway: QueryGateway,
    private val tempplateEngine: TemplateEngine
) {

    @GetMapping("/given-gifts", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun givenGifts() : Flux<ServerSentEvent<HtmlSnippet>> {
        val memberReadModelEntity = queryGateway.query(GetCurrentMember(), MemberReadModelEntity::class.java).join()
        val contribution = queryGateway.query(GetContribution(memberReadModelEntity.memberId),
            ContributionLookUpReadModelEntity::class.java).join()

        val query = queryGateway.subscriptionQuery(
            GivenGiftsOverviewQuery(contribution.contributionId),
            ResponseTypes.instanceOf(GivenGiftsOverviewReadModel::class.java),
            ResponseTypes.instanceOf(GivenGiftsOverviewReadModel::class.java)
        )

        return query.initialResult()
            .concatWith(query.updates())
            .map {
                ServerSentEvent.builder<String>()
                    .event("given-gifts-updated")
                    .data(givenGiftsOverviewComponent(it))
                    .build()
            }
    }

    private fun givenGiftsOverviewComponent(givenGiftsOverviewReadModel: GivenGiftsOverviewReadModel): String {
        val context = Context()
        context.setVariable("givenGiftsOverviewReadModel", givenGiftsOverviewReadModel )
        return tempplateEngine.process("personal-landings-page/fragments/given-gifts", context).replace(Regex("[\\r\\n]"), "")
    }
}