package socialsupermarket.requestsupport

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import socialsupermarket.authentication.AuthenticationEntity
import socialsupermarket.authentication.GetAuthenticationByEmailQuery
import socialsupermarket.authentication.ValidateCredentialsQuery
import socialsupermarket.authentication.jwt.JwtService
import java.util.UUID

data class FinancialSupportRequestForm(
    val requestedFor: UUID,
    val relationShip: String,
    val month: String,
    val amount: Double,
    val notes: String
)


@Controller
@RequestMapping("/supportrequest")
class SupportRequestController(
    val queryGateway: QueryGateway,
    val jwtService: JwtService
) {

    @PostMapping("/login")
    fun handleForm(
        @Valid @ModelAttribute("form") form: FinancialSupportRequestForm,
        bindingResult: BindingResult,
        model: Model,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String? {

        val email = jwtService.getEmailOfUser(request)
        return null
    }
}
