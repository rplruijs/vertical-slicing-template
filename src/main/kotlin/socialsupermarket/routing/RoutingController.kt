package socialsupermarket.routing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import socialsupermarket.authentication.UserContextService
import socialsupermarket.authentication.login.LoginForm
import socialsupermarket.write.registration.RegistrationForm
import socialsupermarket.write.requestsupport.FinancialSupportRequestForm


@Controller
class RoutingController(private val userContextService: UserContextService) {


    @GetMapping("/")
    fun landingPage(model: Model): String {
        return "pages/index"
    }

    @GetMapping("/personal-landings-page")
    fun personalPage(model: Model): String {
        // Add user email to the model
        val userEmail = userContextService.getCurrentUserEmail()
        model.addAttribute("userEmail", userEmail)
        model.addAttribute("requestForm", FinancialSupportRequestForm())

        return "pages/personal-landings-page"
    }

    @GetMapping("/registration")
    fun registration(model: Model) : String {
        model.addAttribute("form", RegistrationForm())
        return "pages/registration"
    }


    @GetMapping("/login")
    fun loginClean(model: Model): String {
        model.addAttribute("form", LoginForm())
        return "pages/login"
    }
}
