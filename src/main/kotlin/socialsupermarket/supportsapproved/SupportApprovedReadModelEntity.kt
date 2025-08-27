package socialsupermarket.supportsapproved

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

data class GetApprovedSupportsQuery(val limit: Int)

@Entity
@Table(name = "supportsapproved")
class SupportApprovedReadModelEntity {

    @Id
    @Column(name = "request_id")
    lateinit var requestId: UUID

    @Column(name = "contribution_id")
    lateinit var contributionId: UUID

    @Column(name = "requested_by")
    lateinit var requestedBy: UUID

    @Column(name = "requested_for")
    lateinit var requestedFor: UUID

    @Column(name = "month")
    lateinit var month: String

    @Column(name = "amount")
    var amount: Double? = null

    @Column(name = "status")
    var status: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SupportApprovedReadModelEntity

        if (amount != other.amount) return false
        if (requestId != other.requestId) return false
        if (contributionId != other.contributionId) return false
        if (requestedBy != other.requestedBy) return false
        if (requestedFor != other.requestedFor) return false
        if (month != other.month) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount?.hashCode() ?: 0
        result = 31 * result + requestId.hashCode()
        result = 31 * result + contributionId.hashCode()
        result = 31 * result + requestedBy.hashCode()
        result = 31 * result + requestedFor.hashCode()
        result = 31 * result + month.hashCode()
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "SupportApprovedReadModelEntity(requestId=$requestId, contributionId=$contributionId, requestedBy=$requestedBy, requestedFor=$requestedFor, month='$month', amount=$amount, status=$status)"
    }


}

data class SupportApprovedReadModel(val data: List<SupportApprovedReadModelEntity>)
