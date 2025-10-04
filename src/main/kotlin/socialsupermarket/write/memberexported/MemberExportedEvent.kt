package socialsupermarket.write.memberexported


import java.time.LocalDate
import java.util.UUID

data class MemberExportedEvent (
    val memberId: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val password: String,
    val currentBalance: Double
)
