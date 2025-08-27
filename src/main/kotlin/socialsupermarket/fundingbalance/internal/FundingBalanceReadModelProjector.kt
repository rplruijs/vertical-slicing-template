package socialsupermarket.fundingbalance.internal

import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import socialsupermarket.events.GiftRegisteredEvent
import socialsupermarket.events.SupportApprovedEvent
import socialsupermarket.fundingbalance.PiggyBankReadModelEntity
import java.time.LocalDateTime

interface PiggyBankReadModelRepository: JpaRepository<PiggyBankReadModelEntity, String> {

    @Query("SELECT e FROM PiggyBankReadModelEntity e WHERE e.id = 1")
    fun getSingleInstance(): PiggyBankReadModelEntity?
}

@Component
class PiggyBankReadModelProjector(val repository: PiggyBankReadModelRepository) {

    @EventHandler
    fun on(event: SupportApprovedEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!

        piggyBankReadModelEntity.currentBalance -= event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
    }

    @EventHandler
    fun on(event: GiftRegisteredEvent) {
        val piggyBankReadModelEntity = repository.getSingleInstance()!!

        piggyBankReadModelEntity.currentBalance += event.amount
        piggyBankReadModelEntity.lastModified = LocalDateTime.now()

        repository.save(piggyBankReadModelEntity)
    }
}