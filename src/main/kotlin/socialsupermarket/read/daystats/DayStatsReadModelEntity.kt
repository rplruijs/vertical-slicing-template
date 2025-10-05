package socialsupermarket.read.daystats

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate


data class DayStatsQuery(val date: LocalDate)
class AllDayStatsQuery()

@Entity
@Table(name = "day_stats")
class DayStatsReadModelEntity {

    @Id
    @Column(name = "date")
    lateinit var date: LocalDate

    @Column(name = "gifts_received")
    var giftsReceived: Int = 0

    @Column(name = "pending_gift_amount")
    var requestsApproved: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DayStatsReadModelEntity

        if (giftsReceived != other.giftsReceived) return false
        if (requestsApproved != other.requestsApproved) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = giftsReceived
        result = 31 * result + requestsApproved
        result = 31 * result + date.hashCode()
        return result
    }

    fun increaseRequestsApproved() {
        requestsApproved++
    }

    fun increaseGiftsReceived() {
        giftsReceived++
    }
}

data class DayStatsReadModel(val date: LocalDate, val giftsReceived: Int, val requestsApproved: Int)
data class AllDayStatsReadModel(val data: List<DayStatsReadModel>)
