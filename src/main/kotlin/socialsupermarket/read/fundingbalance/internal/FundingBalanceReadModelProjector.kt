package socialsupermarket.read.fundingbalance.internal

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.events.SupportApprovedAfterWaitingForFundingEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.events.SupportWaitForFundingEvent
import socialsupermarket.read.fundingbalance.CurrentBalanceQuery
import socialsupermarket.read.fundingbalance.CurrentBalanceReadModel
import socialsupermarket.read.fundingbalance.PiggyBankReadModelEntity
import java.time.LocalDateTime

interface PiggyBankReadModelRepository: JpaRepository<PiggyBankReadModelEntity, Long> {

    @Query("SELECT e FROM PiggyBankReadModelEntity e WHERE e.id = 1")
    fun getSingleInstance(): PiggyBankReadModelEntity?
}

@ProcessingGroup("funding-balance")
@Component
class PiggyBankReadModelProjector(val repository: PiggyBankReadModelRepository,
                                        val queryUpdateEmitter: QueryUpdateEmitter) {

    @EventHandler
    fun on(event: SupportApprovedEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!

        piggyBankReadModelEntity.currentBalance -= event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
        emit(piggyBankReadModelEntity)
    }

    @EventHandler
    fun on (event: SupportWaitForFundingEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!
        piggyBankReadModelEntity.pendingGiftAmount += event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
        emit(piggyBankReadModelEntity)
    }


    @EventHandler
    fun on(event: SupportApprovedAfterWaitingForFundingEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!
        piggyBankReadModelEntity.pendingGiftAmount -= event.amount
        piggyBankReadModelEntity.currentBalance -= event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
        emit(piggyBankReadModelEntity)
    }


    @EventHandler
    fun on(event: GiftRegisteredEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!

        piggyBankReadModelEntity.currentBalance += event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
        emit(piggyBankReadModelEntity)
    }

    private fun emit(piggyBankReadModelEntity: PiggyBankReadModelEntity) {
        queryUpdateEmitter.emit(CurrentBalanceQuery::class.java,
            {true},
            CurrentBalanceReadModel(currentBalance = piggyBankReadModelEntity.currentBalance,
                pendingGiftAmount = piggyBankReadModelEntity.pendingGiftAmount))
    }
}