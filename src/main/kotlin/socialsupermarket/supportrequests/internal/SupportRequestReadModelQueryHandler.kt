package socialsupermarket.supportrequests.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.supportrequests.GetAllSupportRequests
import socialsupermarket.supportrequests.GetSupportRequestQuery
import socialsupermarket.supportrequests.SupportRequestReadModel
import socialsupermarket.supportrequests.SupportRequestsReadModel

@Component
@ProcessingGroup("supportrequests")
class SupportRequestReadModelQueryHandler(val repository: SupportRequestReadModelRepository) {

    @QueryHandler
    fun handle(query: GetSupportRequestQuery): SupportRequestReadModel? {
        val memberReadModel = repository.findById(query.requestId)
            .orElse(null) ?: return null
        return SupportRequestReadModel(data = memberReadModel)
    }

    @QueryHandler
    fun handle(query: GetAllSupportRequests): SupportRequestsReadModel {
        val supportRequests = repository.findAll()
        return SupportRequestsReadModel(data = supportRequests)
    }
}
