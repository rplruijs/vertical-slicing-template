package socialsupermarket.domain.commands.member

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.time.LocalDate
import java.util.UUID

data class CreateMemberAccountCommand(
    @TargetAggregateIdentifier var memberId: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val password: String,
    ): Command