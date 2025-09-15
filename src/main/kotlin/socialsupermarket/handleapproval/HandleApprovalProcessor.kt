package socialsupermarket.handleapproval

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.DisallowReplay
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import socialsupermarket.common.Processor
import socialsupermarket.domain.commands.contribution.HandleApprovalCommand
import socialsupermarket.supportsapproved.GetSupportsQuery
import socialsupermarket.supportsapproved.SupportApprovedReadModel

@Component
@DisallowReplay
class HandleApprovalProcessor : Processor {

    @Autowired lateinit var commandGateway: CommandGateway

    @Autowired lateinit var queryGateway: QueryGateway

    @Scheduled(cron = "*/1 * * * * *")
    fun handleApproveMonthlySupportItem() {
        queryGateway.query(GetSupportsQuery(5, "APPROVED"), SupportApprovedReadModel::class.java)
            .thenApply { model ->
                model.data.forEach { commandGateway.send<HandleApprovalCommand>(
                    HandleApprovalCommand(
                        contributionId = it.contributionId,
                        requestId = it.requestId,
                        month = it.month,
                        requestedBy = it.requestedBy,
                        requestedFor = it.requestedFor,
                        amount = it.amount!!,
                        )
                ) }
            }
    }
}
