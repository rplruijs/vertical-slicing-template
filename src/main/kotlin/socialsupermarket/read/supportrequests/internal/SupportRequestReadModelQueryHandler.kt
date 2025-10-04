package socialsupermarket.read.supportrequests.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.read.supportrequests.GetAllSupportRequests
import socialsupermarket.read.supportrequests.GetRequestsWaitingForFunding
import socialsupermarket.read.supportrequests.SupportRequestsReadModel

@Component
@ProcessingGroup("supportrequests")
class SupportRequestReadModelQueryHandler(val repository: SupportRequestReadModelRepository) {


    @QueryHandler
    fun handle(query: GetAllSupportRequests): SupportRequestsReadModel {
        val supportRequests = repository.findAll()
        return SupportRequestsReadModel(data = supportRequests)
    }

    @QueryHandler
    fun handle(query: GetRequestsWaitingForFunding): SupportRequestsReadModel {
        val supportRequests = repository.findAllByWaitingForFundingIsTrue()
        return SupportRequestsReadModel(data = supportRequests)
    }
}
