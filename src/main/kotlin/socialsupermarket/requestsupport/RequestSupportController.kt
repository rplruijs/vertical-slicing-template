package socialsupermarket.requestsupport

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import socialsupermarket.contributionlookup.ContributionLookUpReadModelEntity
import socialsupermarket.contributionlookup.GetContribution
import socialsupermarket.domain.commands.contribution.RequestSupportCommand
import socialsupermarket.members.GetCurrentMember
import socialsupermarket.members.MemberReadModelEntity
import java.time.LocalDate
import java.util.UUID

data class FinancialSupportRequestForm(

    val requestedFor: UUID? = null,
    val relationShip: String? = null,
    val month: String? = null,
    val amount: Double? = null,
    val notes: String? = null
)


@Controller
@RequestMapping("/supportrequest")
class SupportRequestController(
    val queryGateway: QueryGateway,
    val commandGateway: CommandGateway) {

    @PostMapping("/submit")
    fun handleForm(
        @Valid @ModelAttribute("form") form: FinancialSupportRequestForm,
        bindingResult: BindingResult,
        model: Model,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String? {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form)
            return "support-request-form"
        }

        val memberReadModelEntity = queryGateway.query(GetCurrentMember(), MemberReadModelEntity::class.java).join()
        val contribution = queryGateway.query(GetContribution(memberReadModelEntity.memberId),
            ContributionLookUpReadModelEntity::class.java).join()

        commandGateway.sendAndWait<RequestSupportCommand>(RequestSupportCommand(
            contributionId = contribution.contributionId,
            requestId = UUID.randomUUID(),
            requestedBy = memberReadModelEntity.memberId,
            requestedFor = form.requestedFor!!,
            relationShip = form.relationShip!!,
            month = form.month!!,
            amount = form.amount!!,
            notes = form.notes!!,
            requestedForName = memberReadModelEntity.fullName(),
            requestDate = LocalDate.now()
        ))

        return null
    }
}
