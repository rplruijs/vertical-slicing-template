package socialsupermarket.read.contributionstoclose.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.hibernate.internal.util.collections.CollectionHelper.listOf
import org.springframework.stereotype.Component
import socialsupermarket.read.contributionstoclose.ContributionsToCloseReadModel
import socialsupermarket.read.contributionstoclose.GetContributionToClose
import socialsupermarket.read.contributionstoclose.GetContributionsToClose
import socialsupermarket.events.ContributionYearClosedEvent
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedDeque

@Component
@ProcessingGroup("contributions-to-close")
class ContributionToCloseReadModelQueryHandler(val repository: ContributionToCloseReadModelRepository) {

    val contributionsClosed = ConcurrentLinkedDeque<UUID>()

    @QueryHandler
    fun handle(query: GetContributionToClose): ContributionsToCloseReadModel {
        return repository.findByContributionId(query.contributionId)?.let {
            ContributionsToCloseReadModel(listOf(it))
        } ?: ContributionsToCloseReadModel(emptyList())
    }

    @QueryHandler
    fun handle(query: GetContributionsToClose): ContributionsToCloseReadModel {
        val contributionsToClose = repository.findByCloseDateLessThanEqualOrderByCloseDateAsc(query.closeDate)
            .filter { !contributionsClosed.contains(it.contributionId) }

        return ContributionsToCloseReadModel(data = contributionsToClose)
    }

    @EventHandler
    fun on(event: ContributionYearClosedEvent) {
        while (contributionsClosed.size > 20) {
            contributionsClosed.pollFirst()
        }
        if (contributionsClosed.contains(event.contributionId)) {
            contributionsClosed.add(event.contributionId)
        }
    }
}
