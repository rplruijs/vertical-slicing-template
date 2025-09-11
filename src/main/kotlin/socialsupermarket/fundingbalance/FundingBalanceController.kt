package socialsupermarket.fundingbalance

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

@Controller
class FundingBalanceController(
    private val queryGateway: QueryGateway,
    private val tempplateEngine: TemplateEngine
) {

    @GetMapping("/current-funding-balance", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun friends() : Flux<ServerSentEvent<HtmlSnippet>> {
        val query = queryGateway.subscriptionQuery(
            CurrentBalanceQuery(),
            ResponseTypes.instanceOf(CurrentBalanceReadModel::class.java),
            ResponseTypes.instanceOf(CurrentBalanceReadModel::class.java)
        )

        return query.initialResult()
            .concatWith(query.updates())
            .map {
                ServerSentEvent.builder<String>()
                    .event("current-funding-balance-updated")
                    .data(currentFundingBalanceComponent(it))
                    .build()
            }
    }

    private fun currentFundingBalanceComponent(currentBalanceReadModel: CurrentBalanceReadModel): String {
        val context = Context()
        context.setVariable("currentFundingBalanceReadModel", currentBalanceReadModel )
        return tempplateEngine.process("personal-landings-page/fragments/current-funding-balance", context).replace(Regex("[\\r\\n]"), "")
    }
}