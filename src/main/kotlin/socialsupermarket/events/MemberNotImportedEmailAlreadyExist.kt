package socialsupermarket.events

import java.time.LocalDate

data class MemberNotImportedEmailAlreadyExist(
    val memberId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val birthDate: LocalDate
)