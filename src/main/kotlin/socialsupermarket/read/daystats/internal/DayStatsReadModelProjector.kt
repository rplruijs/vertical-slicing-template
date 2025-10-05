package socialsupermarket.read.daystats.internal

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.read.daystats.DayStatsReadModel
import socialsupermarket.read.daystats.DayStatsQuery
import socialsupermarket.read.daystats.DayStatsReadModelEntity
import java.time.LocalDate

interface DayStatsReadModelRepository : JpaRepository<DayStatsReadModelEntity, LocalDate>

@Component
class DayStatsReadModelProjector(
    private val repository: DayStatsReadModelRepository,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {

    @EventHandler
    fun on(event: SupportApprovedEvent) {
        handleDayStatsUpdate(DayStatsUpdate.RequestApproved(event.approvalDate))
    }

    @EventHandler
    fun on(event: GiftRegisteredEvent) {
        handleDayStatsUpdate(DayStatsUpdate.GiftReceived(event.date))
    }

    private fun handleDayStatsUpdate(update: DayStatsUpdate) {
        val entity = findOrCreateDayStatsEntity(update.date)
        when (update) {
            is DayStatsUpdate.RequestApproved -> entity.increaseRequestsApproved()
            is DayStatsUpdate.GiftReceived -> entity.increaseGiftsReceived()
        }
        repository.save(entity)
        emitUpdate(update.date, entity)
    }

    private fun findOrCreateDayStatsEntity(date: LocalDate): DayStatsReadModelEntity {
        return repository.findById(date).orElse(
            DayStatsReadModelEntity().apply { this.date = date }
        )
    }

    private fun emitUpdate(date: LocalDate, entity: DayStatsReadModelEntity) {
        queryUpdateEmitter.emit(
            DayStatsQuery::class.java,
            { query -> query.date == date },
            DayStatsReadModel(date, entity.giftsReceived, entity.requestsApproved)
        )
    }

    private sealed interface DayStatsUpdate {
        val date: LocalDate

        data class RequestApproved(override val date: LocalDate) : DayStatsUpdate
        data class GiftReceived(override val date: LocalDate) : DayStatsUpdate
    }
}

