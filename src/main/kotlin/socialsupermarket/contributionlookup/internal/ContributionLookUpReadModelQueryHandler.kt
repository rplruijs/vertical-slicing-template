package socialsupermarket.contributionlookup.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.contributionlookup.ContributionLookUpReadModelEntity
import socialsupermarket.contributionlookup.GetContribution

@Component
class ContributionLookUpReadModelQueryHandler(val repository: ContributionLookUpReadModelRepository) {

    @QueryHandler
    fun handle(query: GetContribution): ContributionLookUpReadModelEntity? {
        return repository.findByMemberId(query.memberId)
    }
}
