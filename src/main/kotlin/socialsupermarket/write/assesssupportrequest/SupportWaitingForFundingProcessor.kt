package socialsupermarket.write.assesssupportrequest

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import org.axonframework.queryhandling.QueryGateway
import socialsupermarket.common.Processor
import socialsupermarket.domain.DEFAULT_FUNDING_ID
import socialsupermarket.domain.commands.funding.AssessWaitingSupportRequestCommand
import socialsupermarket.read.supportrequests.GetRequestsWaitingForFunding
import socialsupermarket.read.supportrequests.SupportRequestsReadModel
import java.time.LocalDate

@Component
class SupportWaitingForFundingProcessor(val commandGateway: CommandGateway,
                                             val queryGateway: QueryGateway) : Processor {

    fun run() {

        val supportRequests = queryGateway.query(GetRequestsWaitingForFunding(), SupportRequestsReadModel::class.java)
            .join()

        supportRequests.data.forEach {
            commandGateway.send< AssessWaitingSupportRequestCommand>(
                AssessWaitingSupportRequestCommand(
                    fundingId = DEFAULT_FUNDING_ID,
                    amount = it.amount,
                    requestId = it.requestId,
                    assessDate = LocalDate.now(),
                )
            )
        }
    }
}