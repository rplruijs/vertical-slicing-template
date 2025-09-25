package socialsupermarket.givengifts

import socialsupermarket.common.Event
import socialsupermarket.common.ReadModel
import socialsupermarket.events.SupportGivenEvent
import socialsupermarket.events.SupportRequestedEvent
import java.time.LocalDate
import java.util.UUID

class GivenGiftsOverviewReadModel : ReadModel {

    var givenTotalAmount: Double = 0.0
    var givenApprovedAmount: Double = 0.0
    var givenRequestedAmount: Double = 0.0
    var givenGifts: MutableList<Gift> = mutableListOf()

    fun applyEvents(events: List<Event>) : GivenGiftsOverviewReadModel {
        events.forEach { event ->
            when (event) {
                is SupportRequestedEvent -> {
                    givenRequestedAmount = event.amount
                    givenGifts.add(
                        Gift(
                            requestId = event.requestId,
                            date = event.requestDate,
                            requestedForName = event.requestedForName,
                            reason = event.notes,
                            amount = event.amount,
                            status = "Requested"
                        )
                    )
                }

                is SupportGivenEvent -> {
                    givenApprovedAmount += event.amount
                    givenRequestedAmount -= event.amount
                    givenGifts = givenGifts.stream()
                        .map {
                            if (it.requestId != event.requestId) it
                            else it.copy(status = "Approved")}
                        .toList().toMutableList()

                }
            }
        }
        givenTotalAmount = givenRequestedAmount + givenApprovedAmount
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GivenGiftsOverviewReadModel

        if (givenTotalAmount != other.givenTotalAmount) return false
        if (givenApprovedAmount != other.givenApprovedAmount) return false
        if (givenRequestedAmount != other.givenRequestedAmount) return false
        if (givenGifts != other.givenGifts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = givenTotalAmount.hashCode()
        result = 31 * result + givenApprovedAmount.hashCode()
        result = 31 * result + givenRequestedAmount.hashCode()
        result = 31 * result + givenGifts.hashCode()
        return result
    }
}

data class Gift(
    val requestId: UUID,
    val date: LocalDate,
    val requestedForName: String,
    val reason: String,
    val amount: Double,
    val status: String
)