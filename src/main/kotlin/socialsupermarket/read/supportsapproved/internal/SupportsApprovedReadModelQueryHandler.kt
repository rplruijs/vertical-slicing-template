package socialsupermarket.read.supportsapproved.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import socialsupermarket.read.supportsapproved.GetSupportsQuery
import socialsupermarket.read.supportsapproved.SupportApprovedReadModel

@Component
class SupportsApprovedReadModelQueryHandler(val repository: SupportsApprovedReadModelRepository) {

    @QueryHandler
    fun handle(query: GetSupportsQuery): SupportApprovedReadModel {
        val pageable = PageRequest.of(0, query.limit)
        val approvedSupports = repository.findByStatusIs(query.status, pageable)
        return SupportApprovedReadModel(approvedSupports)
    }

    // TODO make SYNC?
}
