package socialsupermarket.supportrequests

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

data class GetSupportRequestQuery(val requestId: UUID)
class GetAllSupportRequests()

@Entity
@Table(name = "supportrequests")
class SupportRequestReadModelEntity {

    @Id
    @Column(name = "request_id")
    lateinit var requestId: UUID

    @Column(name = "amount")
     var amount: Double = 0.0

    @Column(name = "contribution_id")
    lateinit var contributionId: UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SupportRequestReadModelEntity

        if (amount != other.amount) return false
        if (requestId != other.requestId) return false
        if (contributionId != other.contributionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + requestId.hashCode()
        result = 31 * result + contributionId.hashCode()
        return result
    }
}

data class SupportRequestReadModel(val data: SupportRequestReadModelEntity)
data class SupportRequestsReadModel(val data: List<SupportRequestReadModelEntity>)
