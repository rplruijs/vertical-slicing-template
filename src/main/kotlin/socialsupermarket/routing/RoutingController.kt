package socialsupermarket.routing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import socialsupermarket.authentication.UserContextService
import socialsupermarket.login.LoginForm
import socialsupermarket.registration.RegistrationForm
import socialsupermarket.requestsupport.FinancialSupportRequestForm


@Controller
class RoutingController(private val userContextService: UserContextService) {


    @GetMapping("/")
    fun landingPage(model: Model): String {
        return "index"
    }

    @GetMapping("/personal-landings-page")
    fun personalPage(model: Model): String {
        // Add user email to the model
        val userEmail = userContextService.getCurrentUserEmail()
        model.addAttribute("userEmail", userEmail)
        model.addAttribute("requestForm", FinancialSupportRequestForm())

        return "personal-landings-page/personal-landings-page-extended"
    }

    @GetMapping("/registration")
    fun registration(model: Model) : String {
        model.addAttribute("form", RegistrationForm())
        return "registration"
    }


    @GetMapping("/login")
    fun loginClean(model: Model): String {
        model.addAttribute("form", LoginForm())
        return "login"
    }
}
