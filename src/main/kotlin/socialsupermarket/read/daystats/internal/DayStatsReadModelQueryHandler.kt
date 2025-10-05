package socialsupermarket.read.daystats.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.read.daystats.AllDayStatsQuery
import socialsupermarket.read.daystats.AllDayStatsReadModel
import socialsupermarket.read.daystats.DayStatsReadModel

@Component
class DayStatsReadModelQueryHandler(val repository: DayStatsReadModelRepository) {

    @QueryHandler
    fun handle(query: AllDayStatsQuery) : AllDayStatsReadModel {
        return AllDayStatsReadModel(repository.findAll()
            .map { DayStatsReadModel(it.date, it.giftsReceived, it.requestsApproved) })
    }
}