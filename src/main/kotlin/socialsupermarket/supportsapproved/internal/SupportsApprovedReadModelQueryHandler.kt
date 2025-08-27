package socialsupermarket.supportsapproved.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import socialsupermarket.supportsapproved.GetApprovedSupportsQuery
import socialsupermarket.supportsapproved.SupportApprovedReadModel

@Component
class SupportsApprovedReadModelQueryHandler(val repository: SupportsApprovedReadModelRepository) {

    @QueryHandler
    fun handle(query: GetApprovedSupportsQuery): SupportApprovedReadModel {
        val pageable = PageRequest.of(0, query.limit)
        val approvedSupports = repository.findByStatusIs("APPROVED", pageable)
        return SupportApprovedReadModel(approvedSupports)
    }

    // TODO make SYNC?
}
