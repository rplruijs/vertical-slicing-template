package socialsupermarket.fundingbalance

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime


class CurrentBalanceQuery()

@Entity
@Table(name = "piggybank")
class PiggyBankReadModelEntity {

    @Id
    @Column(name = "id")
    val id: Long = 1  // Fixed ID value of 1 for the single row

    @Column(name = "currentbalance")
    var currentBalance: Double = 0.0

    @Column(name = "pendinggiftamount")
    var pendingGiftAmount: Double = 0.0

    @Column(name = "lastmodificationdate")
    lateinit var lastModified: LocalDateTime
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PiggyBankReadModelEntity

        if (id != other.id) return false
        if (currentBalance != other.currentBalance) return false
        if (pendingGiftAmount != other.pendingGiftAmount) return false
        if (lastModified != other.lastModified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + currentBalance.hashCode()
        result = 31 * result + pendingGiftAmount.hashCode()
        result = 31 * result + lastModified.hashCode()
        return result
    }


}

data class CurrentBalanceReadModel(val currentBalance: Double, val pendingGiftAmount: Double)
