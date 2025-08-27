package socialsupermarket.members

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

class AllMembersQuery()
data class GetMemberQuery(val memberId: UUID)
data class IsEmailAvailableQuery(val email: String)


@Entity
@Table(name = "members")
class MemberReadModelEntity {

    @Column(name = "member_id")
    @Id
    lateinit var memberId: UUID

    @Column(name = "email")
    lateinit var email: String

    @Column(name = "first_name")
    lateinit var firstName: String

    @Column(name = "last_name")
    lateinit var lastName: String

    @Column(name = "birth_date")
    lateinit var birthDate: LocalDate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberReadModelEntity

        if (memberId != other.memberId) return false
        if (email != other.email) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (birthDate != other.birthDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberId.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + birthDate.hashCode()
        return result
    }


}
data class MembersReadModel(val data: List<MemberReadModelEntity>)

