package socialsupermarket.login

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
import socialsupermarket.authentication.GetAuthenticationByEmailQuery
import socialsupermarket.authentication.ValidateCredentialsQuery
import socialsupermarket.authentication.jwt.JwtService

data class LoginForm(
    @field:Email(message = "Invalid email")
    val email: String = "",

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String = ""
)


@Controller
@RequestMapping("/authentication")
class LoginController(
    val queryGateway: QueryGateway,
    val jwtService: JwtService
) {

    @PostMapping("/login")
    fun handleForm(
        @Valid @ModelAttribute("form") form: LoginForm,
        bindingResult: BindingResult,
        model: Model,
        response: HttpServletResponse
    ): String? {

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form)
            return "login-form"
        }

        val validCredentials = queryGateway.query(
            ValidateCredentialsQuery(form.email, form.password),
            Boolean::class.java
        ).join()

        if(!validCredentials) {
            bindingResult.reject("login.invalid", "Invalid email or password")
            model.addAttribute("form", form)
            return "login-form"
        }

        // Get the user details to include in the JWT token
        val authEntity = queryGateway.query(
            GetAuthenticationByEmailQuery(form.email),
            socialsupermarket.authentication.AuthenticationEntity::class.java
        ).join()

        // Generate JWT token and add it as a cookie
        val token = jwtService.generateToken(form.email, authEntity.memberId)
        jwtService.addTokenCookie(response, token)

        response.setHeader("HX-Redirect", "/personal-landings-page")
        return null // Important: null skips view rendering
    }
}
