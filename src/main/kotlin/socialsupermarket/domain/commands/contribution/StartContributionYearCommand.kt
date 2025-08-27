package socialsupermarket.domain.commands.contribution

import org.axonframework.modelling.command.TargetAggregateIdentifier
import socialsupermarket.common.Command
import java.time.LocalDate
import java.util.UUID

data class StartContributionYearCommand(@TargetAggregateIdentifier var contributionId: UUID,
                                        val memberId: UUID,
                                        val startDate: LocalDate): Command