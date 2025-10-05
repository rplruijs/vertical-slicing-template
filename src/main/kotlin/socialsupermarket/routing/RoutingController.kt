package socialsupermarket.routing

import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import socialsupermarket.authentication.UserContextService
import socialsupermarket.authentication.login.LoginForm
import socialsupermarket.read.members.GetCurrentMember
import socialsupermarket.read.members.MemberReadModelEntity
import socialsupermarket.write.registration.RegistrationForm
import socialsupermarket.write.requestsupport.FinancialSupportRequestForm
import java.time.LocalTime


@Controller
class RoutingController(private val userContextService: UserContextService,
                              private val queryGateway: QueryGateway ) {


    @GetMapping("/")
    fun landingPage(model: Model): String {
        return "pages/index"
    }

    @GetMapping("/personal-landings-page")
    fun personalPage(model: Model): String {
        val memberReadModelEntity = queryGateway.query(GetCurrentMember(), MemberReadModelEntity::class.java).join()
        val userEmail = userContextService.getCurrentUserEmail()

        model.addAttribute("userEmail", memberReadModelEntity.email)
        model.addAttribute("welcomeMessage", welcomeMessage(memberReadModelEntity))
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

    private fun welcomeMessage(member: MemberReadModelEntity) : String {
        val currentHour = LocalTime.now().hour
        val greeting = when (currentHour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            in 18..21 -> "Good evening"
            else -> "Good night"
        }
        return "$greeting ${member.fullName()}"
    }
}
