package socialsupermarket.read.fundingbalance.internal

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import socialsupermarket.read.fundingbalance.CurrentBalanceQuery
import socialsupermarket.read.fundingbalance.CurrentBalanceReadModel

@Component
 class FundingBalanceReadModelQueryHandler(val repository: PiggyBankReadModelRepository) {

    @QueryHandler
    fun handle(query: CurrentBalanceQuery): CurrentBalanceReadModel {
        val piggyBank = repository.getSingleInstance()!!
        return CurrentBalanceReadModel(piggyBank.currentBalance, piggyBank.pendingGiftAmount)
    }
}
