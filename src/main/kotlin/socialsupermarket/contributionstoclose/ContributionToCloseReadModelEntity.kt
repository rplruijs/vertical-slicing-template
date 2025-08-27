package socialsupermarket.contributionstoclose

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

import java.util.UUID

data class GetContributionToClose(val contributionId: UUID)
data class GetContributionsToClose(val closeDate: LocalDate)

@Entity
@Table(name = "contributionstoclose")
class ContributionToCloseReadModelEntity {

    @Column(name = "contribution_id")
    @Id
    lateinit var contributionId: UUID

    @Column(name = "member_id")
    lateinit var memberId: UUID

    @Column(name = "close_date")
    lateinit var closeDate: LocalDate
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContributionToCloseReadModelEntity

        if (contributionId != other.contributionId) return false
        if (memberId != other.memberId) return false
        if (closeDate != other.closeDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contributionId.hashCode()
        result = 31 * result + memberId.hashCode()
        result = 31 * result + closeDate.hashCode()
        return result
    }
}

data class ContributionsToCloseReadModel(val data: List<ContributionToCloseReadModelEntity>)