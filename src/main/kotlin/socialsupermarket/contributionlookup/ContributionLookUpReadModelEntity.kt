package socialsupermarket.contributionlookup

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

data class GetContribution(val memberId: UUID)


@Entity
@Table(name = "contributionslookup")
class ContributionLookUpReadModelEntity {

    @Column(name = "member_id")
    @Id
    lateinit var memberId: UUID

    @Column(name = "contribution_id")
    lateinit var contributionId: UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContributionLookUpReadModelEntity

        if (memberId != other.memberId) return false
        if (contributionId != other.contributionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberId.hashCode()
        result = 31 * result + contributionId.hashCode()
        return result
    }

}