package socialsupermarket.read.contributionlookup.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.read.contributionlookup.ContributionLookUpReadModelEntity
import socialsupermarket.read.contributionlookup.GetContribution

@Component
class ContributionLookUpReadModelQueryHandler(val repository: ContributionLookUpReadModelRepository) {

    @QueryHandler
    fun handle(query: GetContribution): ContributionLookUpReadModelEntity? {
        return repository.findByMemberId(query.memberId)
    }
}
