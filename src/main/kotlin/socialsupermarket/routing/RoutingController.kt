package socialsupermarket.routing

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import socialsupermarket.authentication.UserContextService
import socialsupermarket.login.LoginForm
import socialsupermarket.registration.RegistrationForm


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
        model.addAttribute("form", LoginForm())

        return "personal-landings-page-extended"
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
